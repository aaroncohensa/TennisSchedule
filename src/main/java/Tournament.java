import java.io.*;
import java.util.*;
import java.io.File;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Tournament{

    int weekNo;
    String name;
    int tMoney;
    int tPoints;
    int tO1Dist;
    int tO2Dist;
    int tO3Dist;
    int tournid;

    public Tournament(int tournID , int week , String tournName, int prizeMoney , int points , int tOption1Dist , int tOption2Dist , int tOption3Dist){
        this.weekNo =week;
        this.name = tournName;
        this.tMoney= prizeMoney;
        this.tPoints= points;
        this.tO1Dist= tOption1Dist;
        this.tO2Dist= tOption2Dist;
        this.tO3Dist= tOption3Dist;
        this.tournid = tournID;
    }


     public static ArrayList<Tournament> createTournArray(String filePath){
        int NUM_TOURNS=64;
        File file = new File(filePath);   //creating a new file instance
        FileInputStream fis = null;   //obtaining bytes from the file
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //creating Workbook instance that refers to .xlsx file
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet = wb.getSheetAt(0);
        Iterator<Row> itr = sheet.iterator();
        double [] weekNo = new double [NUM_TOURNS];
        String [] tournName = new String [NUM_TOURNS];
        double [] prizeMoney = new double [NUM_TOURNS];
        double [] points = new double [NUM_TOURNS];
        double [] tOpt1 = new double [NUM_TOURNS];
        double [] tOpt2 = new double [NUM_TOURNS];
        double [] tOpt3 = new double [NUM_TOURNS];
        int [] tournIDs = new int [NUM_TOURNS];
        while (itr.hasNext())
        {
            Row row = itr.next();
            Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column
            int i=0;
            int x=0;
            int y=0;
            int z=0;
            int w=0;
            int v=0;
            int a=0;
            int p=0;

            while (cellIterator.hasNext()) // populate all data arrays
            {

                Cell cell = cellIterator.next();
                int rowNumb = row.getRowNum();
                switch (rowNumb)
                {
                    case 0:    //field that represents string cell type
                        int idAtCell = (int)cell.getNumericCellValue();
                        tournIDs[p]=idAtCell;
                        p++;
                        break;
                    case 1:    //field that represents string cell type
                        double numAtCell = cell.getNumericCellValue();
                        weekNo[i]=numAtCell;
                        i++;
                        break;
                    case 2:    //field that represents number cell type
                        String name =cell.getStringCellValue();
                        tournName[x] = name;
                        x++;
                        break;
                    case 3:
                        double doubAtCell = cell.getNumericCellValue();
                        prizeMoney[y]=doubAtCell;
                        y++;
                        break;
                    case 4:
                        double digAtCell = cell.getNumericCellValue();
                        points[z]=digAtCell;
                        z++;
                        break;
                    case 5:
                        double to1D = cell.getNumericCellValue();
                        tOpt1[w]=to1D;
                        w++;
                        break;
                    case 6:
                        double to2D = cell.getNumericCellValue();
                        tOpt2[v]=to2D;
                        v++;
                        break;
                    case 7:
                        double to3D = cell.getNumericCellValue();
                        tOpt3[a]=to3D;
                        a++;
                        break;
                    default:
                }
            }
        }

        ArrayList<Tournament> tournArray = new ArrayList<Tournament>(); //creating array of tournament object
        for(int b =0 ; b<NUM_TOURNS ; b++){
            tournArray.add(new Tournament(tournIDs[b] ,(int)weekNo[b] , tournName[b] ,(int) prizeMoney[b] , (int)points[b] ,(int) tOpt1[b] , (int)tOpt2[b] , (int)tOpt3[b]));
        }
        return tournArray;
    }

    public static void main(String[] args) {

        EdgeWeightedDigraph digraph = new EdgeWeightedDigraph(createTournArray("/Users/aaroncohen/Downloads/TennisScheduleZip/TS/ATP2020Schedul.xlsx"));
        digraph.edges();
        System.out.println(digraph.V());
    }
}


