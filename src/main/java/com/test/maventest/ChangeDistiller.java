	package com.test.maventest;
	
     import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
        import com.google.inject.Guice;
        import com.google.inject.Injector;
//import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
        import ch.uzh.ifi.seal.changedistiller.JavaChangeDistillerModule;
        import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Move;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
public class ChangeDistiller{
    private static FileDistiller createFileDistiller (Language language) {
        //System.out.println("entering code");
        switch (language) {
            case JAVA:
                Injector injector = Guice.createInjector(new JavaChangeDistillerModule());
                //System.out.println("java");
                return injector.getInstance(FileDistiller.class);
            default:
                System.out.println("default");

        }
        return null;
    }
    public static void test(String projName) throws FileNotFoundException{
    	   PrintWriter pw = new PrintWriter(new File(projName+".csv"));
           StringBuilder sb = new StringBuilder();
           sb.append("Commit");
           sb.append(',');
           sb.append("File name");
           sb.append(',');
           sb.append("Entity Type");
           sb.append(',');
           sb.append("Change Type");
           sb.append(',');
           sb.append("Significance Level");
           sb.append('\n');
           File folder = new File("newFile");
           
           File[] listOfFiles = folder.listFiles();
           System.out.println("Change Distiller class");
               for (int i = 0; i < listOfFiles.length; i++) {
                 if (listOfFiles[i].isFile()) {
                   System.out.println("File from change distiller " + listOfFiles[i].getName());
                   String n = listOfFiles[i].getName().toString();
                   String p = n.substring(0,40)+"_p_"+n.substring(43,listOfFiles[i].getName().length()); 
                   System.out.println(n);
                   System.out.println(p);
           File left = new File("./prevFile/"+p);
           File right = new File("./newFile/"+n);
           String commithash = n.toString().substring(0, 40);
           String filename=n.substring(43,listOfFiles[i].getName().length());
          
           
        FileDistiller distiller = ChangeDistiller.createFileDistiller(Language.JAVA);
        try{
            distiller.extractClassifiedSourceCodeChanges(left, right);
            System.out.println("commit count from change :"+SourceCodeExtraction.commit_count);
        } catch(Exception e) {
            System.err.println("Warning: error while change distilling. " + e.getMessage());
        }
      
	    	
        List<SourceCodeChange> changes = distiller.getSourceCodeChanges();
       // System.out.println("changes ---->"+changes);
        if(changes != null) {
        	 for(SourceCodeChange change : changes) {
 		    	if (change instanceof Delete) {
 		    		//System.out.println("Delete");
 		    	}
 		    	else if (change instanceof Insert) {
 		    		//System.out.println("Insert");
 		    	}		
 		    	else if (change instanceof Move) {
 		    		//System.out.println("Move");
 		    	}		  
 		    	else if (change instanceof Update) {
 		    		//System.out.println("Update");
 		    	}		  
           	  sb.append(commithash);
 		    	  System.out.println(commithash);
 		    	sb.append(',');
 		    	sb.append(filename);
 		    	sb.append(',');
 		    	  sb.append(change.getChangedEntity().getType().toString());
 		    	 System.out.println(change.getChangedEntity().getType().toString());
 		    	  sb.append(',');
 		         sb.append(change.getLabel().toString());
 		         System.out.println(change.getChangedEntity().getType().toString());
 		        sb.append(',');
 		       sb.append(change.getSignificanceLevel());
 		       System.out.println(change.getSignificanceLevel());
 		         sb.append('\n');
            	System.out.println(change.getChangedEntity().getType());
            	System.out.println(change.getLabel());

        }
        	 
    }}}
        pw.write(sb.toString());
        pw.close();}}