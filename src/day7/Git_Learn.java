package day7;

public class Git_Learn {

    /*
     VCS(Version control system):
     ->It allows developers to collaborate with others on shared repository,
      and it maintains the changes made to file.
     ->It provides features to merge code, creates the branches for separate line of work,
       and rolling back to previous versions.
     ->Git is one of the popular distributed version control system which allows each developer
       to have a complete copy of the repository, hence allowing offline & independent work capability
       and allows to commit locally without need of constant network connectivity and,
       it provides options to share and collaborate with other developers.
       
       3 main things to note:
       Working directory: initial changes are made here and files are untracked.
       staging area: files are tracked when files moves from working directory to staging area.
       remote repository: changes made in the local can be pushed to remote repository.

       Commands:
       git init ==> initialize the current directory as empty repository.
       git --version ==> provides the installed git version
       git config --list ==> list all the git configuration
       git config --global user.name "username" ==> configure the author's name
       git config --global user.email "userEmail" ==> configure the author's email
       git config user.name ==> shows the author's name.
       git config user.email ==> shows the author's email.
       git config init.defaultBranch main ==> sets main as default branch.
       git branch -m main ==> changes the main branch.
       git status ==> provides the status about tracked/untracked files.
       git add . ==> adds all the files in the current directory to the staging area.
       git add -A ==> adds all the files/folders in the entire repository to the staging area.
       git add "filename"/foldername ==> adds the specified file/folder to the staging area.
       git rm --cached "filename" ==> removes the file from staging area (untrack the file)
       git rm --cached -r . ==> untracks/removes all the files from staging area.
       git rm --cached -rf . ==> untracks all the files from staging area forcefully.
       git rm "filename" ==> removes the tracked file.
       git rm -r "directoryName" ==> removes the tracked directory.
       git commit -m "message" ==> commits changes locally with the given message.
       git commit --amend ==> helps to modify the commit.
       git commit -a -m "message" ==> adds local changes to staging area and commit those changes.
       git log ==> lists all the commits.
       git log [--graph] --oneline ==> provides commit details in single line.
       git log -n ==> lists specified number of commits.
       git branch -a ==> lists all the branches available in local.
       git branch "branchName" ==> creates new branch with the name specified.
       git checkout/switch "branchName" ==> switches from current branch to specified branch. 
       git branch -d "branchName" ==> deletes the specified branch.
       git branch -D "branchName" ==> deletes the specified branch forcefully (when changes are not merged).
       git push origin --delete "branchName" ==> deletes remote repository.
       git ls-files ==> lists all tracked files in the repository.
       git merge "branchName" ==> merges the current branch with specified branch.
       git merge --ff-only "branchName" ==> merges the current branch with specified branch using fast-forward merge.
       git merge --no-ff "branchName" ==> merges the current branch with specified branch without using fast-forward merge.
       git remote add origin "url" ==> adds the remote repository to the local.
       git remote remove origin ==> removes the remote repository from the local.
       git push -u origin "branchName" ==> adds local commits to remote repository.
       git pull origin "branchName" ==> gets remote repo changes into local, and it'll merge the changes.
       git fetch "branchName" ==> gets the remote repo changes into local without merge.
       git stash -m "message" ==> adds the changes to the stash(saving the unfinished work while allowing to do other work)
       git stash list ==> list all the stashes
       git stash apply ==> applies the latest stashed changes.
       git stash drop "stash@{0}" ==> drops the stashed changes.
       git stash pop ==> applies and removes the latest stashed changes.
       git cherry-pick "commitHash" ==> merges the changes present with the commit.
       git reflog ==> shows all commits with the commands.
       git clone "url" ==> creates local repo from remote repo.
       git clean -n ==> shows the untracked files which would remove with clean command.
       git clean -f ==> removes untracked files.
       git clean -df ==> removes untracked folders.
       git clean -xf ==> removes ignored files.
       git diff "commitHash" ==> shows changes made the commit.
       git diff "oldCommitHash" "newCommitHash" ==> shows changes made between the commit.
       git show "commitHash" ==> shows the commit details along with content changes.
       git reset --soft "commitHash" ==> moves the HEAD to the commit mentioned and doesn't remove the change from staging area and working directory.
       git reset --mixed "commitHash" ==> moves the HEAD to the commit mentioned and removes the change from staging area and doesn't working directory.
       git reset --hard "commitHash" ==> moves the HEAD to the commit mentioned and removes the changes from staging area and working directory.
       git reset --soft HEAD~2 ==> removes last two commits.
       git revert "commitHash" ==> creates a commit by removing the changes made by the commit and doesn't remove the commit records.
       git revert "oldCommitHash" "newCommitHash" ==> removes range of commits.
       git blame "filename" ==> shows the changes, author and commit messages for accountability.
       git tag -a "version" -m "tag message" ==> tags the specified version.
       git bisect start ==> starts git binary search to find the bug commit.
       git bisect reset ==> stops git binary search to find the bug commit.
       git bisect good ==> marks commit as good.
       git bisect bad ==> marks commit as bad.
       git rebase -i HEAD~n ==> allows to modify last n commits.
     */
}
