import java.util.ArrayList;
import java.util.HashMap;

public class createTable {

    private static final String columnname= "col-name";
    private static final String columndatatype= "d-t";
    private static final String columnnautoincrement= "a-i";
    private static final String columnnmaxsize= "m-s";
    private static final String columnnullable= "nullable";
    private static final String createtable= "create table ";
    private static final String tablename= "tablename";
    private static final String foreignkeybool="fk-avail";
    private static final String uniquekeybool="uk-avail";

    public void tablecreate(ArrayList<HashMap<String,String>> array){

        ArrayList<HashMap<String,String>> arraylist=array;
        for(int temp1=0;temp1<arraylist.size();temp1++){

            columnseparator(arraylist.get(temp1));

        }

    }


    public void columnseparator(HashMap<String,String> map){

        HashMap<String,String> hashmap=map;
        int columns=Integer.parseInt(hashmap.get("columns"));
        String sqlquery=createtable+hashmap.get(tablename)+"( ";

        for(int temp2=0;temp2<columns;temp2++) {

            if (temp2 == columns - 1) {

                if (hashmap.get(columndatatype + (temp2 + 1)).toLowerCase().equals("varchar")) {

                    sqlquery=sqlquery+forcolumnchar(hashmap, temp2 + 1);

                } else if (hashmap.get(columndatatype + (temp2 + 1)).toLowerCase().equals("int")) {

                    sqlquery=sqlquery+forcolumnint(hashmap, temp2 + 1);

                }


            } else {

                if (hashmap.get(columndatatype + (temp2 + 1)).toLowerCase().equals("varchar")) {

                    sqlquery=sqlquery+forcolumnchar(hashmap, temp2 + 1);

                } else if (hashmap.get(columndatatype + (temp2 + 1)).toLowerCase().equals("int")) {

                    sqlquery=sqlquery+forcolumnint(hashmap, temp2 + 1);

                }

                sqlquery=sqlquery+", ";

            }
        }

        sqlquery=sqlquery+forprimarykey(hashmap);

        if(hashmap.get(foreignkeybool).equals("true")){

            int foreignkeys=Integer.parseInt(hashmap.get("fks"));

            for(int temp3=0;temp3<foreignkeys;temp3++) {

                if(temp3 == foreignkeys - 1){

                    sqlquery=sqlquery+forforeignkey(hashmap,temp3+1);

                }

                else {

                    sqlquery=sqlquery+forforeignkey(hashmap,temp3+1);
                    sqlquery=sqlquery+",";

                }
            }
        }

        if(hashmap.get(uniquekeybool).equals("true")){

            int uniquekeys=Integer.parseInt(hashmap.get("uks"));
            sqlquery=sqlquery+", CONSTRAINT "+hashmap.get("uk-name")+" UNIQUE (";
            for(int temp4=0;temp4<uniquekeys;temp4++){

                if(temp4 == uniquekeys - 1){

                    sqlquery=sqlquery+hashmap.get("uk-col"+(temp4+1));
                    sqlquery=sqlquery+")";

                }

                else {

                    sqlquery=sqlquery+hashmap.get("uk-col"+(temp4+1));
                    sqlquery=sqlquery+",";

                }

            }

        }

        sqlquery=sqlquery+");";

        System.out.println("SQL-QUERY : "+sqlquery+"\n");

    }


    public String forcolumnchar(HashMap<String,String> map, int columnno){

        HashMap<String,String> hashmap=map;
        String columncharquery="";

        columncharquery=columncharquery+map.get(columnname+columnno);
        columncharquery=columncharquery+" varchar("+hashmap.get(columnnmaxsize+columnno)+")";
        if(hashmap.containsKey(columnnullable+columnno)){
            columncharquery=columncharquery+" not null";
        }

        return columncharquery;
    }


    public String forcolumnint(HashMap<String,String> map, int columnno){

        HashMap<String,String> hashmap=map;
        String columnintquery="";

        columnintquery=columnintquery+map.get(columnname+columnno);
        columnintquery=columnintquery+" int";
        if(hashmap.containsKey(columnnullable+columnno)){
            columnintquery=columnintquery+" not null";
        }
        if(hashmap.containsKey(columnnautoincrement+columnno)){
            columnintquery=columnintquery+" AUTO_INCREMENT";
        }

        return columnintquery;
    }


    public String forprimarykey(HashMap<String,String> map){

        HashMap<String,String> hashmap=map;
        String primarykeyquery="";

        primarykeyquery=primarykeyquery+", PRIMARY KEY ("+hashmap.get("pk-column")+")";

        return primarykeyquery;
    }


    public String forforeignkey(HashMap<String,String> map,int foreignkeyno){

        HashMap<String,String> hashmap=map;
        String foreignkeyquery="";

        foreignkeyquery=foreignkeyquery+", CONSTRAINT "+hashmap.get("fkname"+foreignkeyno)+"  FOREIGN KEY ("+hashmap.get("fklocalcol"+foreignkeyno)+") REFERENCES "+hashmap.get("fkreftable"+foreignkeyno)+"("+hashmap.get("fkrefcol"+foreignkeyno)+")";

        if(hashmap.containsKey("fkconstraints"+foreignkeyno)){

            foreignkeyquery=foreignkeyquery+" "+hashmap.get("fkconstraints"+foreignkeyno);

        }

        return foreignkeyquery;
    }


}