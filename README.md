# ChangeDistillerWrapper

## What is Change Distiller Wrapper:
##### Change Distiller Wrapper as the name suggests is a tool that wraps around the functionality provided by Change Distiller.
##### On a quick note, Change distiller takes in two files and compares them and provides different file level metrics. More information can be founf at:
(https://bitbucket.org/sealuzh/tools-changedistiller/wiki/Home). 

##### Change Distiller Wrapper takes in the functionality of change distiller and wraps around a layer that lets you get metrics per commit level. The drawback with change Distiller is that you always need to provide two input files for comparison. However with Change Distiller Wapper, you can download the git repo and provide the path of the .git file, and the csv file containing the list of commits you want to take into consideration, Change Distiller Wrapper dynamically retrieves the files for each commit and compares it with the previous version of the file. It then compares the metrics for each file in a commit and sums up all the values to retrieve the value at commit level. Finally you will get a csv file containing all the data.

### Steps to perform:
##### 1. Clone the repository to local system.
##### 2. Download/ Move the file containing the valid commit for the reasarch into the local repository folder.
##### 3. Rename the file name as in a format - "ProjectName_commits.csv"
##### 4. Check for any issues in your IDE. In case there are some issues with pom.xml or maven, download the jars from Change Distiller or JGit based on which issue is being encountered.
##### 5. Run the program
##### 6. Pass the .git file path of local repository as input
##### 7. Once the execution is complete an output file containing changes of made to the files for a particular revision will be stored in a csv file with name same as project name.

##### The credits for the tool Change Distiller go to the original creator. As mentioned above, this tool uses change distiller and JGit to provide results at a different level
