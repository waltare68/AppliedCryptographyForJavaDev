package fr.eseo.i3.acfjd.securefs.managers;
import java.util.Date;
import java.sql.Timestamp;
import java.io.File;
import javax.swing.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import java.util.Formatter;
import java.util.Scanner;
import java.util.stream.Stream;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;



import java.util.Arrays;
import java.util.Base64;

import fr.eseo.i3.acfjd.securefs.i18n.I18n;

public class SolutionSecureFileSystemManager extends AbstractSecureFileSystemManager{
	private JPasswordField pwdPassword;
	private JTextField txtUsername;
	public PublicKey spubkey=null;
	private static SecretKeySpec secretKey;
	private static byte[] key;
	
	Timestamp ts=new Timestamp(System.currentTimeMillis());
	    
	   
	
	
	@Override
	public void delete(File[] files) {
		// TODO Auto-generated method stub
		String fname = null;
		 if (files != null) {
		      for (int i = 0, n = files.length; i < n; i++) {
		    	  files[i].delete();
		    	   fname = files[i].getName();
		    	  JFrame j;
		    	 j=new JFrame();
		    	  JOptionPane.showMessageDialog(j,"file Deleted Successfully.");
		    	 //log the action 
		    	try {
		    		File file = new File("C:/Users/ROBOWALT/secure/log.txt");
		        	 String log="\n deleted the files "+fname+"\n";
		        	 BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
				        writer.append("\n"+log+ts);
				        
				        writer.close();
		    		
		    	}catch(Exception e) {
		    		
		    	} 
		    	  
		      }

		    }
		  		 		
	}
	
	//setting key and encrypt functions
    public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
 
    public static String decrypt(String strToDecrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }


	@Override
	public void encryptDecrypt(File[] files) {
		// TODO Auto-generated method stub
		//we will use the status to figure out if a file is encrypted or not,1 for encrpted and 0 for decrypted
		
		 final String secretKey = "ssshhhhhhhhhhh!!!!";
		 String encryptedString = null;
		String ext="enc";
		String fname = null;
		if (files != null) {
		      for (int i = 0, n = files.length; i < n; i++) {
		    	  fname=files[i].getName();
		    	  System.out.println("file name: " + fname);
		    	  if(files[i].isDirectory()) {
			        	System.out.println("we got a dir mate");
			        	//well so if its dir we recursively digest each file in there with another for loop mans
			        	File f = new File("C:/Users/ROBOWALT/secure/"+fname);
			        	File filesList[] = f.listFiles();
			            System.out.println("List of files and directories in the specified directory:");
			            Scanner sc = null;
			            try {
				        	   for(File file : filesList) {
				        		     String fileName=file.getName();
				        		     String FilePath=file.getAbsolutePath();
				        	         System.out.println("File name: "+file.getName());
				        	         System.out.println("File path: "+file.getAbsolutePath());
				        	       //Instantiating the Scanner class
				        	         sc= new Scanner(file);
				        	         String input;
				        	         StringBuffer sb = new StringBuffer();
				        	         while (sc.hasNextLine()) {
				        	            input = sc.nextLine();
								        System.out.println(input);
				        	            try {
				        	            	 int k = fileName.lastIndexOf('.');
									    	  if (k > 0) {
									    		String extension = fileName.substring(k+1);
									    		System.out.println(extension);
							    		
							    	    //check if file has .enc extension
							    	      if(!extension.equals(ext)){
							    	    	  //we encrypt the file
							    	    	  System.out.println("encrypt the file");
							    	    	  encryptedString = SolutionSecureFileSystemManager.encrypt(input, secretKey) ;
							    	    	  System.out.println(encryptedString);
							    	    	  try {
							    	    		File fil=new File("C:/Users/ROBOWALT/secure/"+"/"+fname+"/"+fileName+".enc"); 
							    	    		if(!fil.exists()) {
							    	    			fil.createNewFile();
							    	    		}
							    	    		PrintWriter pw=new PrintWriter(fil);
							    	    		pw.println(encryptedString);
							    	    		pw.close();
							    	    		
							    	    	  }
							    	    	  catch(Exception e) {e.printStackTrace();}
							    	    	
							    	      }
							    	      else {
							    	    	  System.out.println("Decrypt the file");
							    	    	  System.out.println("Data to be Decrypted "+input);
							    	    	  encryptedString= input;
											String decryptedString = SolutionSecureFileSystemManager.decrypt(encryptedString, secretKey) ;
											System.out.println(decryptedString);  
											 try {
								    	    		File fil=new File("C:/Users/ROBOWALT/secure/"+"/"+fname+"/"+fileName+".dec"); 
								    	    		if(!fil.exists()) {
								    	    			fil.createNewFile();
								    	    		}
								    	    		PrintWriter pw=new PrintWriter(fil);
								    	    		pw.println(decryptedString);
								    	    		pw.close();
								    	    		
								    	    	  }
								    	    	  catch(Exception e) {e.printStackTrace();}
							    	      }
							    	      
							    	      
							    	      
							    	      }
									    	  else {}
				        	            	
				        	            }catch(Exception e) {
				        	            	
				        	            }
				        	         }
				        	   }
				        }catch(Exception e) {
				        	
				        }
			        }
			        else {
			        	try {	
				    		  File filz=new File("C:/Users/ROBOWALT/secure/"+fname);
								 Scanner Sc=new Scanner(filz);
								 while(Sc.hasNextLine()) {
									    String data = Sc.nextLine();
								        System.out.println(data);
						    	  int k = fname.lastIndexOf('.');
						    	  if (k > 0) {
						    		String extension = fname.substring(k+1);
				    		
				    	    //check if file has .enc extension
				    	      if(!extension.equals(ext)){
				    	    	  //we encrypt the file
				    	    	  System.out.println("encrypt the file");
				    	    	  encryptedString = SolutionSecureFileSystemManager.encrypt(data, secretKey) ;
				    	    	  System.out.println(encryptedString);
				    	    	  try {
				    	    		File fil=new File("C:/Users/ROBOWALT/secure/"+fname+".enc"); 
				    	    		if(!fil.exists()) {
				    	    			fil.createNewFile();
				    	    		}
				    	    		PrintWriter pw=new PrintWriter(fil);
				    	    		pw.println(encryptedString);
				    	    		pw.close();
				    	    		
				    	    	  }
				    	    	  catch(Exception e) {e.printStackTrace();}
				    	    	
				    	      }
				    	      else {
				    	    	  System.out.println("Decrypt the file");
				    	    	  System.out.println("Data to be Decrypted "+data);
				    	    	  encryptedString= data;
								String decryptedString = SolutionSecureFileSystemManager.decrypt(encryptedString, secretKey) ;
								System.out.println(decryptedString);  
								 try {
					    	    		File fil=new File("C:/Users/ROBOWALT/secure/"+fname+".dec"); 
					    	    		if(!fil.exists()) {
					    	    			fil.createNewFile();
					    	    		}
					    	    		PrintWriter pw=new PrintWriter(fil);
					    	    		pw.println(decryptedString);
					    	    		pw.close();
					    	    		
					    	    	  }
					    	    	  catch(Exception e) {e.printStackTrace();}
				    	      }
				    	      
				    	      
				    	      
				    	      }
						    	  else {}
				    	     
				    	  } 
								 
						    	  
							
							
				    	  }
				    	  catch(Exception e) {
				    		  
				    	  }

			        	
			        	
			        }
		      
		    	  
		    	  
		      }}
		
		
				    			
	}	
	// Read my file in my Users/MyName/secure/pswd.txt
	// the folder secure is a folder that i have create, you have to create and put the pswd.txt into
	public String readFile() {
		String line;
		String userText = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("C:/Users/ROBOWALT/secure/pswd.txt")));
          
            while((line = reader.readLine()) != null){
                
                	userText = line;
                	System.out.println("Read the data encrypted: " + userText);
            }
            
        } catch (Exception ex){
            System.err.println("Error. "+ex.getMessage());
        }
        return userText;
     
    }
	
	//encoding with cipher SHA
	 public static byte[] getSHA(String input) throws NoSuchAlgorithmException 
	    {  
	        // Static getInstance method is called with hashing SHA  
	        MessageDigest md = MessageDigest.getInstance("SHA-256");  
	  
	        // digest() method called  
	        // to calculate message digest of an input  
	        // and return array of byte 
	        return md.digest(input.getBytes(StandardCharsets.UTF_8));  
	    } 
	    
	 //parameter specifies the number which is to be converted to a Hexadecimal string
	    public static String toHexString(byte[] hash) 
	    { 
	        // Convert byte array into signum representation  
	        BigInteger number = new BigInteger(1, hash);  
	  
	        // Convert message digest into hex value  
	        StringBuilder hexString = new StringBuilder(number.toString(16));  
	  
	        // Pad with leading zeros 
	        while (hexString.length() < 32)  
	        {  
	            hexString.insert(0, '0');  
	        }  
	        return hexString.toString();  
	    } 
	
	@Override
	public boolean isPasswordCorrect(String user, char[] password) {
		String compare = readFile();
		String encrypt = "";
		try {
			//read in the pswd.txt and when they are "Login :" and "Password :" 
			encrypt = toHexString(getSHA("Login : " + user + " " + "Password : " + String.valueOf(password)));
			System.out.print(encrypt);
			//create the log file
		    File file = new File("C:/Users/ROBOWALT/secure/log.txt");
		    String program="\n "+  ts +" " +user+" logged in the system ";
		    try {

		      // create a new file with name specified
		      // by the file object
		      boolean value = file.createNewFile();
		      if (value) {
		        System.out.println("\n New log File is created.");
		        //write to log
		        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		        writer.write(program);
		        
		        writer.close();
		      }
		      else {
		        System.out.println("The log-file already exists.");
		        BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
		        writer.append(program);
		        
		        writer.close();
		      }
		    }
		    catch(Exception e) {
		      e.getStackTrace();
		    }
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//compare password and login with the to validate the userText that we read
		if (compare.equals(encrypt)) {
			return true;
		}else  {
			return false;
		}

	}

	@Override
	public void sign(File[] files) throws NoSuchAlgorithmException  {
		// TODO Auto-generated method stub
		String fname = null;	
		 if (files != null) {
		      for (int i = 0, n = files.length; i < n; i++) {
		    	  fname=files[i].getName();
		    	  System.out.println("file name: " + fname);
		    	  if(files[i].isDirectory()) {
			        	System.out.println("we got a dir mate");
			        	//sign all the files in the dir
			        	File f = new File("C:/Users/ROBOWALT/secure/"+fname);
			        	File filesList[] = f.listFiles();
			            System.out.println("List of files and directories in the specified directory:");
			            Scanner sc = null;
			            try {
				        	   for(File file : filesList) {
				        		     String fileName=file.getName();
				        		     String FilePath=file.getAbsolutePath();
				        	         System.out.println("File name: "+file.getName());
				        	         System.out.println("File path: "+file.getAbsolutePath());
				        	         //Instantiating the Scanner class
				        	         sc= new Scanner(file);
				        	         String input;
				        	         StringBuffer sb = new StringBuffer();
				        	         while (sc.hasNextLine()) {
				        	            input = sc.nextLine();
				        	            try {
				        	        		
									        // the rest of the code goes here
									    	   //Creating KeyPair generator object
									    	      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
									    	      
									    	      //Initializing the key pair generator
									    	      keyPairGen.initialize(2048);
									    	      
									    	      //Generate the pair of keys
									    	      KeyPair pair = keyPairGen.generateKeyPair();
									    	      
									    	      //Getting the private key from the key pair
									    	      PrivateKey privKey = pair.getPrivate();
									    	      PublicKey pubkey=pair.getPublic();
									    	      
									    	      spubkey=pubkey;
									    	     
									    	      //Creating a Signature object
									    	      Signature sign = Signature.getInstance("SHA256withDSA");
									    	      
									    	      //Initialize the signature
									    	      sign.initSign(privKey);
									    	      byte[] bytes = input.getBytes();
									    	      
									    	      //Adding data to the signature
									    	      sign.update(bytes);
									    	      
									    	      //Calculating the signature
									    	      byte[] signature = sign.sign();
									    	      
									    	     
									    	      File myObj = new File("C:/Users/ROBOWALT/secure/"+"/"+fname+"/" +fileName+".signature.txt");
									    	      if (myObj.createNewFile()) {
									    	        System.out.println("File created: " + myObj.getName());
									    	      } else {
									    	        System.out.println("File already exists.");
									    	      }
									    	      try {
									    	          FileWriter myWriter = new FileWriter("C:/Users/ROBOWALT/secure/"+"/"+fname+"/"+fileName+".signature.txt");
									    	          myWriter.write(new String(signature, "UTF8"));
									    	          myWriter.close();
									    	          System.out.println("Successfully wrote to the file.");
									    	          JFrame j;
														 j=new JFrame();
														JOptionPane.showMessageDialog(j,"signature is generated,try verifying the signaature");
									    	        } catch (IOException e) {
									    	          System.out.println("An error occurred.");
									    	          e.printStackTrace();
									    	        }
									    	      
									    	    //Printing the signature
									    	      System.out.println("Digital signature for given text: "+new String(signature, "UTF8"));
					
									        } catch (Exception e) {
									            System.err.println("Caught exception " + e.toString());
									        }

				        	            
				        	         }
				        	   }
			            }  
				       catch(Exception e) {}
			        	
			        	
			        }
			        else {
			        	fname=files[i].getName();
			        	System.out.println("file name: " + fname);
						try {
							 File filz=new File("C:/Users/ROBOWALT/secure/"+fname);
							 Scanner Sc=new Scanner(filz);
							 while(Sc.hasNextLine()) {
								    String data = Sc.nextLine();
							        System.out.println(data);
				     

						       try {
		
						        // the rest of the code goes here
						    	   //Creating KeyPair generator object
						    	      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
						    	      
						    	      //Initializing the key pair generator
						    	      keyPairGen.initialize(2048);
						    	      
						    	      //Generate the pair of keys
						    	      KeyPair pair = keyPairGen.generateKeyPair();
						    	      
						    	      //Getting the private key from the key pair
						    	      PrivateKey privKey = pair.getPrivate();
						    	      PublicKey pubkey=pair.getPublic();
						    	      
						    	      spubkey=pubkey;
						    	     
						    	      //Creating a Signature object
						    	      Signature sign = Signature.getInstance("SHA256withDSA");
						    	      
						    	      //Initialize the signature
						    	      sign.initSign(privKey);
						    	      byte[] bytes = data.getBytes();
						    	      
						    	      //Adding data to the signature
						    	      sign.update(bytes);
						    	      
						    	      //Calculating the signature
						    	      byte[] signature = sign.sign();
						    	      
						    	     
						    	      File myObj = new File("C:/Users/ROBOWALT/secure/"+fname+".signature.txt");
						    	      if (myObj.createNewFile()) {
						    	        System.out.println("File created: " + myObj.getName());
						    	      } else {
						    	        System.out.println("File already exists.");
						    	      }
						    	      File file = new File("C:/Users/ROBOWALT/secure/log.txt");
							        	 String log="\n signed the files "+fname+"\n" ;
							        	 BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
									        writer.append("\n"+log+ts);
									        
									        writer.close();
						    	      try {
						    	          FileWriter myWriter = new FileWriter("C:/Users/ROBOWALT/secure/"+fname+".signature.txt");
						    	          myWriter.write(new String(signature, "UTF8"));
						    	          myWriter.close();
						    	          System.out.println("Successfully wrote to the file.");
						    	          JFrame j;
											 j=new JFrame();
											JOptionPane.showMessageDialog(j,"signature is generated,try verifying the signaature");
						    	        } catch (IOException e) {
						    	          System.out.println("An error occurred.");
						    	          e.printStackTrace();
						    	        }
						    	      
						    	    //Printing the signature
						    	      System.out.println("Digital signature for given text: "+new String(signature, "UTF8"));
		
						        } catch (Exception e) {
						            System.err.println("Caught exception " + e.toString());
						        }

				      }
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        	
			        }	    
		    	  }

		      }else{
		    	  
		      }
		 }
	

	@Override
	public void verify(File[] files) {
		// TODO Auto-generated method stub
		//get the last data
		//calculate a new signature with the public key,first we get the public key
		//the previous signature
		//compare the two signatures
		
		String thedata=null;
		String newsign=null;
		String oldsign=null;
		String fname = null;	
		 if (files != null) {
		      for (int i = 0, n = files.length; i < n; i++) {
		    	  fname=files[i].getName();
		    	  System.out.println("file name: " + fname);
		    	  }
		      try {
		    	  File filz=new File("C:/Users/ROBOWALT/secure/"+fname);
					 Scanner Sc=new Scanner(filz);
					 while(Sc.hasNextLine()) {
						    String data = Sc.nextLine();
					        System.out.println(data);
					        //this data contains our old signature
					        
					        oldsign=data;
					        
					 
					 
					 }  	  
		    	  
		      }
		      catch(Exception e) {
	        	  //  Block of code to handle errors
	        	}
		      try {
		    	  int iend=fname.indexOf("signature.txt");
		    	  String prevFname ;
		    	  if(iend !=-1) {
		    		  prevFname= fname.substring(0 , iend); 
		    		 // we recalculate this digest again
		    		  System.out.println("The file used to create the sign: " + prevFname);
		    	  File filz=new File("C:/Users/ROBOWALT/secure/"+prevFname);
					 Scanner Sc=new Scanner(filz);
					 while(Sc.hasNextLine()) {
						   String  data = Sc.nextLine();
					        System.out.println(data);
					        thedata=data;
					        
					 }
					//input the public key
			        	//spubkey is our public key
			        	System.out.println("public key"+spubkey);
			        	//input the signature bytes 
			    	     byte[] sigtoverify =oldsign.getBytes();
			    	     //initialise the signature obj
			    	     Signature sign = Signature.getInstance("SHA256withDSA");
			    	     sign.initVerify(spubkey);
			    	     //update and verify the data
			    	     byte[] bytes = thedata.getBytes();
			    	      sign.update(bytes);
			    	      System.out.println("well done");
			    	      
			    	      //lets try verify this sig ,maybe it wil verify
			    	      boolean verifies = sign.verify(sigtoverify);
			    	      
			    	      if(verifies) {
			    	          System.out.println("Signature verified");   
			    	       } else {
			    	          System.out.println("Signature failed");
			    	       }
			              System.out.println("signature is wrong");
			              
		    	  } }catch(Exception e) {
	        	  //  Block of code to handle errors
	        	}
		        try {
		        	
		        	
		        }catch(Exception e) {
		        	  //  Block of code to handle errors
		        
		        
		        }

		 }
		 
	
		 
		 
		 
		 
	}

	@Override
	public void displayLog() {
		// TODO Auto-generated method stub
		
		try {
			Runtime rt=Runtime.getRuntime();
			Process p = Runtime.getRuntime().exec("notepad C:/Users/ROBOWALT/secure/log.txt");
			System.out.println("log should show up");
		}catch(Exception e) {
			
		}
	}

	@Override
	public void displayStatus() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void createDigest(File[] files) throws NoSuchAlgorithmException{
		//TODO Auto-generated method stub
		
		
		String fname = null;	
		 if (files != null) {
		      for (int i = 0, n = files.length; i < n; i++) {
			        if(files[i].isDirectory()) {
			        	System.out.println("we got a dir mate");
			        	fname=files[i].getName();
			        	//well so if its dir we recursively digest each file in there with another for loop mans
			        	File f = new File("C:/Users/ROBOWALT/secure/"+fname);
			        	File filesList[] = f.listFiles();
			            System.out.println("List of files and directories in the specified directory:");
			            Scanner sc = null;
			           try {
			        	   for(File file : filesList) {
			        		     String fileName=file.getName();
			        		     String FilePath=file.getAbsolutePath();
			        	         System.out.println("File name: "+file.getName());
			        	         System.out.println("File path: "+file.getAbsolutePath());
			        	         //Instantiating the Scanner class
			        	         sc= new Scanner(file);
			        	         String input;
			        	         StringBuffer sb = new StringBuffer();
			        	         while (sc.hasNextLine()) {
			        	            input = sc.nextLine();
			        	            try {
							        	  //  Block of code to try
							        	MessageDigest md = MessageDigest.getInstance("SHA-256");
								    	  md.update(input.getBytes());
								    	  md.update(input.getBytes());
								    	  byte[] digest = md.digest();      
								          System.out.println(digest); 
								          StringBuffer hexString = new StringBuffer();
								          for (int j = 0;j<digest.length;j++) {
								              hexString.append(Integer.toHexString(0xFF & digest[j]));
								           }
								           System.out.println("Hex format : " + hexString.toString());
								           try{    
								               FileWriter fw=new FileWriter("C:/Users/ROBOWALT/secure/"+"/"+fname+"/" +fileName+".digest");    
								               fw.write(hexString.toString()); 
								               JFrame j;
								      		 j=new JFrame();
								      		JOptionPane.showMessageDialog(j,"Digest created success.");
								               fw.close();    
								              }catch(Exception e){System.out.println(e);} 
							        	
							        	
							        	
							        	}
							        catch(Exception e) {
							        	  //  Block of code to handle errors
							        	}
							        	
			        	            sb.append(input+" ");
			        	         }
			        	         System.out.println("Contents of the file: "+sb.toString());
			        	         System.out.println(" ");
			        	      } 
			        	   
			           }
			           catch(Exception e){}

			            
			        }
			        else {
			        	fname=files[i].getName();
			        	System.out.println("file name: " + fname);
			        	 try {
							 File filz=new File("C:/Users/ROBOWALT/secure/"+fname);
							 Scanner Sc=new Scanner(filz);
							 while(Sc.hasNextLine()) {
								    String data = Sc.nextLine();
							        System.out.println(data);
							        try {
							        	  //  Block of code to try
							        	MessageDigest md = MessageDigest.getInstance("SHA-256");
								    	  md.update(data.getBytes());
								    	  md.update(data.getBytes());
								    	  byte[] digest = md.digest();      
								          System.out.println(digest); 
								          StringBuffer hexString = new StringBuffer();
								          for (int j = 0;j<digest.length;j++) {
								              hexString.append(Integer.toHexString(0xFF & digest[j]));
								           }
								           System.out.println("Hex format : " + hexString.toString());
								           try{    
								               FileWriter fw=new FileWriter("C:/Users/ROBOWALT/secure/"+fname+".digest");    
								               fw.write(hexString.toString()); 
								               JFrame j;
								      		 j=new JFrame();
								      		JOptionPane.showMessageDialog(j,"Digest created success.");
								               fw.close();    
								              }catch(Exception e){System.out.println(e);} 
							        	
							        	
							        	
							        	}
							        catch(Exception e) {
							        	  //  Block of code to handle errors
							        	}
							 }
				      }
				      catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        }
			
			       
		      }
		 }
		 else {}
		
		
		
		
		
   }		    
	

	@Override
	public void verifyDigest(File[] files) {
		// TODO Auto-generated method stub we recalculate the previous digest and compare with this digest
		
		String fname = null;
		String origDigest = null;
		String newDigest = null;
		
		 if (files != null) {
		      for (int i = 0, n = files.length; i < n; i++) {
		    	  fname=files[i].getName();
		    	  System.out.println("file name: " + fname);
		    	  
		    	  
		    	  
		    	  int iend=fname.indexOf(".digest");
		    	  String prevFname;
		    	  if(iend !=-1) {
		    		  prevFname= fname.substring(0 , iend); 
		    		 // we recalculate this digest again
		    		  System.out.println("The file used to create the digest: " + prevFname);
		    	      
		          
		    		  try {
							File filz=new File("C:/Users/ROBOWALT/secure/"+prevFname);
							 Scanner Sc=new Scanner(filz);
							 while(Sc.hasNextLine()) {
								    String newDigestdata = Sc.nextLine();
							        System.out.println(newDigestdata);
							        try {
							        	  //  Block of code to try
							        	 MessageDigest md = MessageDigest.getInstance("SHA-256");
								    	  md.update(newDigestdata.getBytes());
								    	  md.update(newDigestdata.getBytes());
								    	  byte[] digest = md.digest();      
								          System.out.println(digest); 
								          StringBuffer hexString = new StringBuffer();
								          for (int j = 0;j<digest.length;j++) {
								              hexString.append(Integer.toHexString(0xFF & digest[j]));
								              
								           }
								          
								          
							        	newDigest=hexString.toString();
							        	 System.out.println("Hex format for our new digest : " + newDigest);
							        	 File file = new File("C:/Users/ROBOWALT/secure/log.txt");
							        	 String log="\n verified digest of files "+fname+"\n";
							        	 BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));
									        writer.append("\n"+log+ts);
									        
									        writer.close();
							        	
							        	}
							        	catch(Exception e) {
							        	  //  Block of code to handle errors
							        	}
							
							 }
						}	
						catch (Exception e){
							
							 System.out.println(e);
						 }
					try {
						File filz=new File("C:/Users/ROBOWALT/secure/"+fname);
						 Scanner Sc=new Scanner(filz);
						 while(Sc.hasNextLine()) {
							     origDigest = Sc.nextLine();
						        System.out.println(origDigest);
						        
						
						 }
					}	
					catch (Exception e){
						
						 System.out.println(e);
					 } 
					//System.out.println(origDigest+"this was the original digest");
					//we verify the digest by comparing the two digests
					 System.out.println(origDigest.equals(newDigest));// should be true
					if(origDigest.equals(newDigest)) {
						JFrame j;
						j=new JFrame();
						JOptionPane.showMessageDialog(j,"verification success.");	
					}
					else {
						JFrame j;
						 j=new JFrame();
						JOptionPane.showMessageDialog(j,"data is corrupted.");
					}
	}

	}
		   
		 }
	}
}