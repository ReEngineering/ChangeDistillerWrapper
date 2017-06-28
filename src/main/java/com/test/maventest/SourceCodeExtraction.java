package com.test.maventest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;


/**
 * Hello world!
 *
 */
public class SourceCodeExtraction 
{
	public static String repoName;
	static int commit_count=0;
	static int newCount=0;
	static int oldCount=0;
	static int changeclass=0;
	
	public static void main(String[] args) throws IOException, GitAPIException 
	{
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the local repository path for the project under study:");
		repoName = scanner.next();
		repoName = repoName.replaceAll("\\\\","/");
		int lastIndexOfrepoName=repoName.lastIndexOf("/");
		String tmprepoName=repoName.substring(1,lastIndexOfrepoName-1);
		int repoNameUpdated=tmprepoName.lastIndexOf("/");
		System.out.println("Updated Repository name: "+repoName);
		//System.out.println(in);
		String projName=repoName.substring(repoNameUpdated+2, lastIndexOfrepoName);
		System.out.println("Project Name: "+projName);
		Repository repo = new FileRepository(repoName);
//System.out.println(repo);
		Git git = new Git(repo);
		new File("./newFile").mkdir();
		new File("./prevFile").mkdir();
		CSVReader.csvReader(projName,repo,git);
	ChangeDistiller.test(projName);
deleteDirectory("./newFile");
deleteDirectory("./prevFile");
	}
	public static void deleteDirectory(String directoryFilePath) throws IOException
	{
	    Path directory = Paths.get(directoryFilePath);

	    if (Files.exists(directory))
	    {
	        Files.walkFileTree(directory, new SimpleFileVisitor<Path>()
	        {
	            @Override
	            public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException
	            {
	                Files.delete(path);
	                return FileVisitResult.CONTINUE;
	            }

	            @Override
	            public FileVisitResult postVisitDirectory(Path directory, IOException ioException) throws IOException
	            {
	                Files.delete(directory);
	                return FileVisitResult.CONTINUE;
	            }
	        });
	    }
	}
	
	public static void getFileDetails(String SHA,Repository repo, Git git, String projName) throws RevisionSyntaxException, AmbiguousObjectException, IncorrectObjectTypeException, IOException
	{
		try 
		{
			RevWalk walk = new RevWalk(repo);
			//String commit = "85fa0c7d7a27e3aa2da549de4db2e506ebee2efb";
			ObjectId commitId = repo.resolve(SHA);
			ObjectId s = git.getRepository().resolve(SHA+"^");
			String s1 = s.toString();
			String s2 = s1.substring(12);
			String prevCommit = s2.replaceAll("]+$", "");
			System.out.println("commit: "+commitId);
			System.out.println("prevCommit: "+ prevCommit);
			ObjectId prevCommitId = repo.resolve(prevCommit);
			try (ObjectReader reader = git.getRepository().newObjectReader())
			{
				CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
				ObjectId oldTree = git.getRepository().resolve(SHA+"^^{tree}");
				oldTreeIter.reset(reader, oldTree);
				CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
				ObjectId newTree = git.getRepository().resolve(SHA+"^{tree}");
				newTreeIter.reset(reader, newTree);
	
				DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
				diffFormatter.setRepository(git.getRepository());
				diffFormatter.setDetectRenames(true);
				List<DiffEntry> entries = diffFormatter.scan(oldTreeIter, newTreeIter);
			
				RevCommit commit1 = walk.parseCommit(commitId);
				commit_count++;
				System.out.println("Commit count: "+commit_count);
				RevCommit commit2 = walk.parseCommit(prevCommitId);
				String[] statusStore = new String[100];
				String[] fileSplit = new String[100];
				int i = 0;
				int k=0;
				for (DiffEntry entry : entries) 
				{
					String file = entry.toString();
	
					if (file.endsWith(".java]")) 
					{
						String splitDiffEntry = file.substring(10);
						String[] splitStatus = splitDiffEntry.split("\\s+");
					//	System.out.println(splitStatus[0]);
						System.out.println( "File updated in the commit");
						String s12=splitStatus[0];
						String finalFile = splitStatus[1].replaceAll("]+$", "");
						fileSplit[i] = finalFile;
					//	System.out.println(finalFile);
						statusStore[i] = splitStatus[0];
						System.out.println("Status " + statusStore[i]);
						int flag=0;
						String status;
						/*if(splitStatus[0].equals("ADD"))
						{
							k=i;
							flag=1;
							
						}*/
						
					//	System.out.println("i"+i);
						//System.out.println("k"+k);
						switch (splitStatus[0])
						{
							case "MODIFY": 
							{
							TreeWalk tw = TreeWalk.forPath(reader, finalFile, commit1.getTree());
	
							ObjectLoader objectLoader = reader.open(tw.getObjectId(0));
							objectLoader.openStream();
							ByteArrayOutputStream contentToBytes = new ByteArrayOutputStream();
							objectLoader.copyTo(contentToBytes);
							// System.out.println(contentToBytes);
							System.out.println("Previous file compared (Modify): "+finalFile);
							status = "newFile";
							//calling write function for previous file
							File oldFile = writeFile(finalFile, SHA, contentToBytes, status);
							TreeWalk twold = TreeWalk.forPath(reader, finalFile, commit2.getTree());
							ObjectLoader objectLoader1 = reader.open(twold.getObjectId(0));
							objectLoader1.openStream();
							ByteArrayOutputStream contentToBytes1 = new ByteArrayOutputStream();
							objectLoader1.copyTo(contentToBytes1);
							System.out.println("New file compared (Modify): "+finalFile);
							status = "prevFile";
							//calling write function for current file
							File newFile = writeFile(fileSplit[i], SHA, contentToBytes1, status);
							break;
							}
							case "RENAME":
							{
								String[] renameFile=finalFile.split("->");
								String oldFile=renameFile[0];
								System.out.println("Renamed oldFIle:"+ oldFile);
								String newFile=renameFile[1];
								System.out.println("Renamed newFile:" +newFile);
								TreeWalk tw = TreeWalk.forPath(reader, newFile, commit1.getTree());
								ObjectLoader objectLoader = reader.open(tw.getObjectId(0));
								objectLoader.openStream();
								ByteArrayOutputStream contentToBytes = new ByteArrayOutputStream();
							objectLoader.copyTo(contentToBytes);
							// System.out.println(contentToBytes);
							status = "newFile";
							writeFile(newFile, SHA, contentToBytes, status);
							TreeWalk twold = TreeWalk.forPath(reader, oldFile , commit2.getTree());
							ObjectLoader objectLoader1 = reader.open(twold.getObjectId(0));
							objectLoader1.openStream();
							ByteArrayOutputStream contentToBytes1 = new ByteArrayOutputStream();
							objectLoader1.copyTo(contentToBytes1);
							// System.out.println(contentToBytes);
							status = "prevFile";
							writeFile(oldFile , SHA, contentToBytes1, status);
							break;
							}
							default:
							break;
						}
						//i++;
					}
					// git checkout
				}

			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	


	private static File writeFile(String fileName, String commitID, ByteArrayOutputStream contentToBytes,
			String status) {
		// TODO Auto-generated method stub


		OutputStream outStream = null;
		ByteArrayOutputStream byteOutStream = null;
		File currentFile=null;
		File previousFile=null;
		try {
			Path p = Paths.get(fileName);
			String shortFileName = p.getFileName().toString();
			if (status == "newFile") {

				String fName = commitID + "_n_" + shortFileName;
newCount++;
System.out.println("new File count: "+newCount);
				outStream = new FileOutputStream("./newFile/"+fName);
			//	System.out.println("test " + outStream);
				byteOutStream = new ByteArrayOutputStream();
				// writing bytes in to byte output stream
				contentToBytes.writeTo(outStream); // data
				// byteOutStream.writeTo(outStream);
				return currentFile;
			} else {
				String fName = commitID + "_p_" + shortFileName;

				outStream = new FileOutputStream("./prevFile/"+fName);
				oldCount++;
				System.out.println("new File count: "+oldCount);
				byteOutStream = new ByteArrayOutputStream();
				// writing bytes in to byte output stream
				contentToBytes.writeTo(outStream); // data
				byteOutStream.writeTo(outStream);
				return previousFile;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}
}
