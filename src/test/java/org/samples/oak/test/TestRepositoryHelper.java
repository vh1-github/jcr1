package org.samples.oak.test;

import org.samples.oak.demo.bean.AssetDetail;
import org.samples.oak.demo.bean.FileResponse;
import org.samples.oak.demo.bean.VersionHistory;
import org.samples.oak.demo.service.RepoHistoryHelper;
import org.samples.oak.demo.service.RepositoryBuilder;
import org.samples.oak.demo.service.RepositoryHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

public class TestRepositoryHelper {
    private static Repository repo;
    
    @SuppressWarnings("resource")
	public static void main(String[] args) {
    	
    	int key;
		System.out.println("Enter the type of operation(1-4) :");
		System.out.println("1: create node.");
		System.out.println("2: add file.");
		System.out.println("3: Get file content.");
		System.out.println("4: version history.");
		System.out.println("5: get assets.");
		System.out.println("6: edit file.");
		key = new Scanner(System.in).nextInt();
		doSetup();
		switch (key) {
		case 1:
			shouldCreateNodes();		
			break;
		case 2:
			shoudAddFile();		
			break;
		case 3:
			testGetFileContent();
			break;
		case 4:
			testGetVersionHistory();
			break;
		case 5:
			shouldGetAssets();
			break;
		case 6:
			shouldEditFile();
			break;
		case 7:
			testRestoreVersion();
			break;
		default:
			System.out.println("Wrong choice!");
			break;
		}		
	}

    
    public static void shouldEditFile() {
        try {
            Session session = getSession();
            RepositoryHelper.editFile(session, "/testNode/deal/subdeal", new File("C:\\Users\\Harshit\\Desktop\\tex1.txt"), "bishnu");
            System.out.println("File Edit Complete");
            cleanUp(session); // do this in finally
        } catch (RepositoryException | IOException e) {
            e.printStackTrace();
        }
    }

    
    public static void shouldGetAssets() {
        String basePath = "/testNode/deal/subdeal";
        try {
            Session session = getSession();
            System.out.println("Starting the asset fetch...");
            List<AssetDetail> assets = RepositoryHelper.getAssets(session, basePath);
            System.out.println(assets.size());
            for (AssetDetail assetDetail : assets) {
                System.out.println(assetDetail);
            }
            cleanUp(session); // do this in finally
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    
    public static void testGetVersionHistory() {
        try {
            Session session = getSession();
            List<VersionHistory> versions = RepoHistoryHelper.getVersionHistory(session, "/testNode/deal/subdeal",
                    "tex1.txt");
            for (VersionHistory versionHistory : versions) {
                System.out.println(versionHistory);
            }
            cleanUp(session); // do this in finally
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    
    public static void testRestoreVersion() {
        try {
            Session session = getSession();
            RepoHistoryHelper.restoreVersion(session, "134b68c8-38e4-46fe-bdb7-b1f7887d8ac6");
            cleanUp(session); // do this in finally
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    
    public void shouldDeleteNode() {
        try {
            Session session = getSession();
            RepoHistoryHelper.deleteVersionHistories(session, "/testNode/deal/subdeal", "C:\\Users\\Harshit\\Desktop\\tex1.txt");
            RepositoryHelper.deleteNode(session, "/testNode/deal/subdeal", "C:\\Users\\Harshit\\Desktop\\tex1.txt");
            cleanUp(session); // do this in finally
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
    public static void testGetFileContent() {
        try {
            Session session = getSession();
            System.out.println("Fething the file...");
            FileResponse fileResponse = RepositoryHelper.getFileContents(session, "/testNode/deal/subdeal",
                    "tex1.txt");
            byte[] content = fileResponse.getBytes();
            if (content != null && content.length > 0) {
                FileOutputStream fos = new FileOutputStream("D:/" + "tex1_copy.txt");
                fos.write(content);
                fos.close();
                System.out.println("File fetch complete...");
            }
            cleanUp(session); // do this in finally
        } catch (RepositoryException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    
    public static void shoudAddFile() {
        try {
            System.out.println("Adding the file...");
            Session session = getSession();

            RepositoryHelper.addFileNode(session, "/testNode/deal/subdeal", new File("C:\\Users\\Harshit\\Desktop\\tex1.txt"), "admin");

            System.out.println("Files added...");
            cleanUp(session); // do this in finally
        } catch (RepositoryException | IOException e) {
            e.printStackTrace();
        }
    }

    
    public static void shouldCreateNodes() {
        try {
            Session session = getSession();
            RepositoryHelper.createNodes(session, "/testNode/deal3");
            System.out.println("Node created...");
            cleanUp(session); // do this in finally
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static Session getSession() throws LoginException, RepositoryException {
        if (repo != null)
            return repo.login(new SimpleCredentials("admin", "admin".toCharArray()));
        else
            throw new NullPointerException("Repository not initialized");
    }

    public static void doSetup() {
        try {
            repo = RepositoryBuilder.getRepo("localhost", 27017);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private static void cleanUp(Session session) {
        if (session != null) {
            session.logout();
        }
    }
}
