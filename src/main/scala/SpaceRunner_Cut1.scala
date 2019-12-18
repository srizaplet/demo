
import scalafx.Includes._
import scalafx.animation.AnimationTimer
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.input._
import scalafx.scene.canvas.Canvas
import scalafx.scene.media.AudioClip
import scalafx.scene.paint._
import scalafx.scene.image.Image
import scalafx.scene.shape._
import scalafx.scene.text._
import scalafx.scene.{Group, Scene}
import scala.collection.mutable.ArrayBuffer
import util.control.Breaks._


import scala.collection.mutable
import scala.collection.mutable.Map
import scala.collection.mutable.LinkedHashSet

class enemies (var i : Image, val id : Int ,var x : Double, var y : Double) {

  var image = i
  val uid = id
  var axisX = x
  var axisY = y

}




object SpaceRunner_Cut1 extends JFXApp {
  var enemies1  = List(mutable.Map(new Image("resources/Galaxy2.jpg") -> (mutable.Map(2.0 -> 4.5))))

  var uniqueId : Int = 0
  stage = new PrimaryStage {
    var root = new Group()
    scene = new Scene(root,800,800) {
      title = "SPACE RUNNER"
      var canvas = new Canvas(800,800)
      var g = canvas.getGraphicsContext2D

      var backGroundImage = new Image("resources/Galaxy2.jpg")
      var imageY = 0.0

      // Initialize Player and Enemies
      var player = new Image("resources/SpaceShip.gif")
      var plX = 500.0
      var plY = 500.0

      var e = new Image("resources/Alien.gif")
      var randX = math.random*800
      var randY = math.random*800
      var enemiesObj : mutable.ArrayBuffer[enemies] = mutable.ArrayBuffer(new enemies(e,uniqueId,randX,randY))
      var bullets = List(Ellipse(800,800,3,6))

      // Initialize Player and enemies color


      bullets.head.fill = Color.Red

      // Initialize/Add the Content in the Scene
      content = List(canvas)

      // Capturing the moving keys Right/Left/Up,Down

      var leftPressed = false
      var rightPressed = false
      var upPressed = false
      var downPressed = false
      var spacePressed = false

      onKeyPressed = (e:KeyEvent) => {
        if(e.code == KeyCode.Left) leftPressed = true
        if(e.code == KeyCode.Right) rightPressed = true
        if(e.code == KeyCode.Up) upPressed = true
        if(e.code == KeyCode.Down) downPressed = true
        if(e.code == KeyCode.Space) spacePressed = true
      }

      onKeyReleased = (e:KeyEvent) => {
        if(e.code == KeyCode.Left) leftPressed = false
        if(e.code == KeyCode.Right) rightPressed = false
        if(e.code == KeyCode.Up) upPressed = false
        if(e.code == KeyCode.Down) downPressed = false
        if(e.code == KeyCode.Space) {
          //spacePressed = false
          val b = Ellipse(plX + 30, plY, 3,12)
          b.fill = Color.Red
          content += b
          bullets ::= b
        }
      }

      //Initilize time and Speed and Spawning
      var lastTime = 0L
      var enemySpeed = 60
      var playerSpeed = 5.0
      var spawnDelay = 2.0


      val timer : AnimationTimer = AnimationTimer( t => {

        g.drawImage(backGroundImage,0.0,imageY,800,800)
        g.drawImage(backGroundImage,0.0,imageY-800,800,800)
        g.drawImage(player,plX,plY,60,100)
        g.setFont(new Font("Arial", 200))

        imageY += 1.0
        if(imageY - 800 >= 0) imageY = 0.0

        if(lastTime > 0){

          val delta = (t - lastTime)/1e9
          var dist = 0.0
          var ctr : Int = 0

          if (enemiesObj.length > 0) {
            breakable {
              for (a <- enemiesObj) {

                var ctr: Int = 0
                var objId: Int = ctr
                val dx = plX - a.axisX
                val dy = plY - a.axisY
                dist = math.sqrt(dx * dx + dy * dy)
                val drawX = (a.axisX + dx / dist * enemySpeed * delta)
                val drawY = (a.axisY + dy / dist * enemySpeed * delta)
                g.drawImage(a.image, drawX, drawY, 30, 40)
                a.axisX = drawX
                a.axisY = drawY
                //println(a.axisX, a.axisY)


                if ((dist < 5 + 10)) {
                  val musicfile = new AudioClip("resources/gameover2.mp3")
                  musicfile.play()
                  val endText = new Text(250, 300, "Sree Game over   ")
                  endText.fill = Color.Yellow
                  content += endText
                  timer.stop
                }


                for (b <- bullets) {

                  b.centerX = b.centerX.value
                  b.centerY = b.centerY.value - 3
                  if (b.centerY.value < 0) {
                    content -= b
                  }
                  else {
                    var dist1 = 1000.0
                    val dx1 = b.centerX.value - a.axisX
                    val dy1 = b.centerY.value - a.axisY
                    dist1 = math.sqrt(dx1 * dx1 + dy1 * dy1)
                    //println(a)
                    if (dist1 < (b.radiusX.value + 20)) {
                      //println("This is inside : " + ctr + " :  " + enemiesObj.length)
                      val musicfile = new AudioClip("resources/bullet.wav")
                      //musicfile.play()
                      //timer.wait(1000)
                      //timer.wait(3000)
                      content -= b
                      println(enemiesObj.length)
                      if (enemiesObj.length > 0) {
                        musicfile.play()
                        enemiesObj -= a
                        break()
                      }

                      //println(enemiesObj)
                    }

                  } //else
                }
                ctr += 1
                if (enemiesObj.length < 0) {break()}
              }
            }
          }


          if(leftPressed) {
            plX = plX - playerSpeed
            g.drawImage(player, plX, plY, 60, 100)
          }

          if(rightPressed) {
            plX = plX + playerSpeed
            g.drawImage(player,plX,plY,60,100)
          }

          if(upPressed) {
            plY = plY - playerSpeed
            g.drawImage(player,plX,plY,60,100)
          }

          if(downPressed) {
            plY = plY + playerSpeed
            g.drawImage(player,plX,plY,60,100)
          }
          //if(upPressed) player.centerY = player.centerY.value - playerSpeed*delta
          //if(downPressed) player.centerY = player.centerY.value + playerSpeed*delta

          if(spacePressed) {

            //            val b = Circle(plX + 30, plY, 3)
            //            b.fill = Color.Red
            //            content += b
            //            bullets ::= b

          }



          spawnDelay -= delta
          if(spawnDelay < 0) {
            uniqueId +=1
            val e = new Image("resources/Alien.gif")
            var randX = math.random*800
            var randY = math.random*800
            enemiesObj += new enemies(e,uniqueId,randX,randY)
            spawnDelay = 2.0
          }

        }
        lastTime = t
      })
      timer.start()


    }
  }
}