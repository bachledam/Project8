import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

    class Lab {
	  private int crn;
	  private String roomNumber;
	
	  public Lab(int crn, String roomNumber) {
	    this.crn = crn;
	    this.roomNumber = roomNumber;
	  }
	
	  public int getCRN() {
	    return crn;
	  }
	
	  public String getRoomNumber() {
	    return roomNumber;
	  }
	}

class Lecture {
  private int crn;
  private String prefix;
  private String title;
  private String type;
  private String buildingCode;
  private boolean isOnline;
  private boolean hasLabs;
  private ArrayList<Lab> labs;
  private String roomNumber;
  public char[] getCRN;
  public Lecture(int crn, String prefix, String title, String type, String buildingCode, String roomNumber, boolean hasLabs) {
	  this.crn = crn;
	  this.prefix = prefix;
	  this.title = title;
	  this.type = type;
	  this.buildingCode = buildingCode;
	  this.roomNumber = roomNumber;
	  this.hasLabs = hasLabs;
	  if (this.hasLabs) {
		  labs = new ArrayList<Lab>();
	  }
  }

  public void addLab(int crn, String roomNumber) {
	  labs.add(new Lab(crn, roomNumber));
  }

  public boolean isOnline() {
	  return isOnline;
  }
  public boolean hasLabs() {
	  return hasLabs;
}
  public String getRoomNumber() {
	  return roomNumber;
}

  public ArrayList < Lab> getLabs() {
	  return labs;
}
  public int getCRN() {
	  return crn;
}	
@Override
public String toString() {
  if (isOnline()) {
	  return String.format("%d, % s, % s, % s, Online", crn, prefix, title, type);
  }
	  else {
		  return String.format("%d, %s, %s, %s, %s, %s, %s", crn, prefix, title, type, buildingCode, roomNumber, hasLabs() ? "Yes" : "No");
	  }
	}
}

public class HW10 {
  public static ArrayList<Lecture> readLectures(String fileName) {
    ArrayList<Lecture> lectures = new ArrayList<Lecture>();

    File inputFile = new File(fileName);

    boolean readLabs = false;

    try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
      String line;

      while ((line = br.readLine()) != null) {
        String[] fields = line.trim().split(",");

        if (readLabs) {
          if (fields.length != 2) {
            readLabs = false;

          } else {
            int crn = Integer.parseInt(fields[0].trim());

			String roomNumber = ((String) fields[1]).trim();

            lectures.get(lectures.size() - 1).addLab(crn, roomNumber);

            continue;
          }
        }
        int crn = Integer.parseInt(fields[0].trim());
        String prefix = fields[1].trim();
        String title = fields[2].trim();
        String type = fields[3].trim();
        
        if (fields[4].trim().equalsIgnoreCase("ONLINE")) {
        	lectures.add(new Lecture(crn, prefix, title, type, line, fileName, readLabs));
        } else {
        	String buildingCode = fields[4].trim();
        	String roomNumber = fields[5].trim();
        	boolean hasLabs = false;
        	
        	if (fields[6].trim().equalsIgnoreCase("YES")) {
        		hasLabs = true;
        		readLabs = true;
        	}
        	lectures.add(new Lecture(crn, prefix, title, type, buildingCode, roomNumber, hasLabs));
        }
      }

        } catch (IOException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
    return lectures;
  }
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    ArrayList<Lecture> LECTURES = readLectures("lec.txt");
    int numOnline = 0;
    for (Lecture lecture : LECTURES) {
      if (lecture.isOnline()) {
         numOnline++;
        } 
    }
	System.out.printf("There are %d online lectures offered\n", numOnline);
	System.out.print ("Enter the classroom: ");
	
	String roomNumber = input.nextLine();
	
	for (Lecture lecture : LECTURES) {
	      if (!lecture.isOnline()) {
	        if (lecture.getRoomNumber().equalsIgnoreCase(roomNumber)) {
	        	System.out.println("The crns held in" + lecture.getRoomNumber() + lecture.getCRN());
	        }
		if (lecture.hasLabs()) {
			ArrayList<Lab> labs = lecture.getLabs();
		  for (Lab lab : labs) {
			  if (lab.getRoomNumber().equalsIgnoreCase(roomNumber)) {
				  System.out.println("The crns held in " + lab.getRoomNumber() + " are: " +lab.getCRN());
			}
		  }
		}
	  }
	}
	  input.close();
	
	  String output = "lecturesOnly.txt";
	
	  try { PrintWriter writer = new PrintWriter(output);
		    writer.write("Online Lectures \n");
			for (Lecture lecture : LECTURES) {
				if (lecture.isOnline()) {
					writer.write(lecture.toString() + "\n");
				}
			}
	
			writer.write("\nLectures With No Labs\n");
			for (Lecture lecture : LECTURES) {
				if (lecture.isOnline() && !lecture.hasLabs()) {
					writer.write(lecture.toString() + "\n");
				}
			}	
	    writer.close();
		System.out.printf("%s is created. \n", output);
		  } catch (FileNotFoundException ex) {
	    ex.printStackTrace();
	  }
	}
}