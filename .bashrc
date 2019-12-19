source /Users/sree/git_AutoComplete.bash
parse_git_branch() {
    git branch 2> /dev/null | sed -e '/^[^*]/d' -e 's/* \(.*\)/ (\1)/'
}


PS1_wdir="\[$(tput sgr0)\]\[\033[38;5;24m\]\W"
PS1_gitbranch="\e[38;5;204m\]\$(parse_git_branch)"
PS1_gt="\[$(tput bold)\]\[$(tput sgr0)\]\[\e[38;5;214m\]>"
PS1_other="\[$(tput sgr0)\]\[$(tput sgr0)\]\[\e[38;5;15m\]"


export PS1="${PS1_wdir}${PS1_gitbranch}${PS1_gt}${PS1_other} \[$(tput sgr0)\]"
echo ""
echo ""
echo "Welcome Sree .. Bash Set with Git AutoComplete and Current Git Branch as Prompt"
echo ""
echo ""
