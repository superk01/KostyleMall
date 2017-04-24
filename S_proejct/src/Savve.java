import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Savve {
   private int startRow = 0;
   private int productLinkRow = 0;
   private int endRow = 0;

   String frontURL = "http://www.66girls.co.kr/product/search.html?banner_action=&keyword=ÄÚÆ®"; //À°À°°ÉÁî
   //String frontURL = "http://www.michyeora.com/shop/search.php?keyword=%EC%BD%94%ED%8A%B8"; //¹ÌÃÄ¶ó ÄÚÆ® %C4%DA%C6%AE& //¼Ò³à³ª¶óÀÇ ÄÚÆ®
  // String frontURL = "http://ggsing.com/product/search.html?banner_action=&keyword=ÄÚÆ®";//°í°í½Ì
      
   //String frontURL = "http://imvely.com/product/search.html?view_type=&supplier_code=&category_no=&search_type=product_name&keyword=ÄÚÆ®"; // ÀÓºí¸®
   // String searchURL = "ÄÚÆ®"; //°Ë»öÇÒ´Ü¾î
   // String endURL = "&page=1";
   String abc = frontURL;

   List<String> list = new ArrayList<String>();
   List<Integer> startLiRowNum = new ArrayList<Integer>(); // ¹°Ç° ½ÃÀÛ box ÁÙ ¸ğÀ½
   List<Integer> endLiRowNum = new ArrayList<Integer>(); // ¹°Ç° ³¡ box ÁÙ ¸ğÀ½
   

   public void writeFile() {

      try {
         URL url = new URL(abc);
         URLConnection con = url.openConnection();
         
         BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
         String tmp;

         while ((tmp = br.readLine()) != null) {
            list.add(tmp);
            System.out.println(tmp);
         }
         System.out.println("¿Ï·á");
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         // ¹°Ç° ÇÏ³ª ½ÃÀÛ ÅÂ±× ÀÎ½Ä
         String startLI = "<li id";
         String startClass = "xans-record"; // ¼îÇÎ¸ô ¹°Ç° ¹Ú½º class ÀÌ¸§

         // ¹°Ç° ÇÏ³ª ³¡ ÅÂ±× ÀÎ½Ä
         String endLI = "</li>"; // ÀÌ°Ç row ÀüÃ¼°ªÀÌ </li>ÀÌ¿©¾ß ÇÑ´Ù.

         // ¹°Ç° ÇÏ³ª ¹Ú½º »ı¼º

         // ¹°Ç° ¹Ù·Î°¡±â ¸µÅ© ÆäÀÌÁö ÀÎ½Ä
         String productLink[] = { "a href=\"/product/detail", "a href=\"/shop/view" };
         String product_NameCheck = "[°¡-ÆR]"; // ¹°Ç° ÀÌ¹ÌÁö °Ë»ö (ÇÑ±Û ÀÖÀ¸¸é true ¸®ÅÏ)
         int productImgLinkRowNum = 0;// ÀÌ¹ÌÁö src ´ã°ÜÀÖ´Â ¿­ °Ë»ç°á°ú

         String Not_product_NameCheck = "[^°¡-ÆR]"; // ¹°Ç° ÀÌ¸§ ÃßÃâ ÃÖ´ë±æÀÌ 10, ¹°Ç° ±İ¾×Àº
                                          // ÇÑ±Û 4±ÛÀÚ ÀÌÇÏ, ¹°Ç° ¼³¸íÀº ÇÑ±Û
                                          // 10ÀÚ ÀÌÇÏ ÇÑ±Û °Ë»ç ÀÖÀ¸¸é
                                          // false¸®ÅÏ
         int productLinkRowNum = 0; // °á°ú°ª ÀúÀå

         for (int i = 0; i < list.size(); i++) {
            String row = list.get(i);
            int startlicheck = -1;
            int startliclasscheck = -1;
            startlicheck = row.indexOf(startLI);
            startliclasscheck = row.indexOf(startClass);

            if (startlicheck != -1 && startliclasscheck != -1) {
               startLiRowNum.add(i);
               System.out.println("startBoxRow Ãß°¡¿Ï·á");
            } else if (row.trim().equals(endLI) && startLiRowNum.size() > 0) {
               if (startLiRowNum.get(0) < i) {
                  endLiRowNum.add(i);
               }
            }
            
         }

         for (int i = 0; i < startLiRowNum.size(); i++) {
            int startrow = startLiRowNum.get(i);
            int endrow = endLiRowNum.get(i);            
            
            for(int j=startrow; j<endrow; j++){
               String row = list.get(j);
               
               //¹°°Ç ¸µÅ©°ª
               //¹°°Ç ¼³¸í°ª
               //¹°°Ç ÀÌ¸§°ª
               //¹°°Ç ±İ¾× °ª
               String ImageLink = "";//¹°°Ç ÀÌ¹ÌÁö ¸µÅ©°ª
               
               
               for(int p=0; p<productLink.length; p++){
                  int pLinkNum = row.indexOf(productLink[p]);
//                   System.out.println(row.replaceAll("[^°¡-ÆR]","").length() + "////" + row);
                  
                  if( pLinkNum != -1 && row.replaceAll("[^°¡-ÆR]","").length() > 0){ //ÀÌ ÁÙ¿¡ ¸µÅ©°¡ ÀÖ°í ÇÑ±ÛÀÌ ÀÖÀ¸¸é
                     //¸µÅ© °ª ÃßÃâ
                     //ÇÑ±Û °ª ÃßÃâ
                     System.out.println(row);
                     break;
                  }else if(pLinkNum != -1 &&  row.replaceAll("[^°¡-ÆR]","").length() == 0){ //¸¸¾à¿¡ ¸µÅ©¸¸ ÀÖ°í ÇÑ±ÛÀÌ ¾øÀ¸¸é ÀÌ¹ÌÁö ¸µÅ©µµ ÀÖÀ¸´Ï ÀÌ¹ÌÁö ¸µÅ© ÃßÃâ
                     System.out.println(j);
                     ImageLink= searchImgLink(row);      
                     System.out.println(ImageLink);
                  }
               }
               
               
            }
            
         }

      }   
   }
   
    //¹°Ç° ¹Ù·Î°¡±â ¸µÅ© ÆäÀÌÁö ÀÎ½ÄÇÏ¸é ÀÌ¹ÌÁö¸µÅ© ÃßÃâ
    public String searchImgLink(String row){
        
       String pattern[] = {".gif",".jpg",".png"};
       
        int productImgLinkRow = row.indexOf("<img src");        //¹°Ç° ¹Ù·Î°¡±â¿¡¼­ ¿Ê ÀÌ¹ÌÁö ¸µÅ© ÁÙ¹øÈ£ °¡Á®¿À±â

        String productLinkResult = "";        //¹°Ç° ¹Ù·Î°¡±â¿¡¼­ ¿Ê ÀÌ¹ÌÁö ¸µÅ© °¡Á®¿À±â
        if( productImgLinkRow != -1){
           String productLinkRow = row; //ex ) aaaaA<img src = "dddddd">ddd
           String productLinkRow_Frist_Cut = productLinkRow.substring(productImgLinkRow, productLinkRow.length()); //ex) <img src="ddddddd">ddd
           int productLinkRow_End_Cut_Index = -1;
           for(int i=0; i<pattern.length; i++){
              if( productLinkRow_Frist_Cut.indexOf(pattern[i]) != -1){
              productLinkRow_End_Cut_Index = productLinkRow_Frist_Cut.indexOf(pattern[i])+5; //³¡°ª ½Äº° .gif .png .jpg
              }
           }
           productLinkResult = productLinkRow_Frist_Cut.substring(10, productLinkRow_End_Cut_Index-1); // <img src="ddddddd">
        }
        return productLinkResult;
    }
    
   public static void main(String args[]) {
      /*
       * List<String> list = new ArrayList<String>(); list.
       * add("adfsdfsdafasdf<img src=\"ddhdhdhdh.dhÀÌ°ÍÀºÇÑ±ÛÀÔ´Ï´Ùdh\">ddÀÌ°ÍÀºÇÑ±ÛÀÔ´Ï´Ùdddddd<src <img <src img ><src img <img src > >dd"
       * ); int productLinkRowNum = 0; //°á°ú°ª ÀúÀå //¹°Ç° ¹Ù·Î°¡±â¿¡¼­ ¿Ê ÀÌ¹ÌÁö ¸µÅ© ÁÙ¹øÈ£ °¡Á®¿À±â
       * int productImgLinkRow =
       * list.get(productLinkRowNum).indexOf("<img src"); //¹°Ç° ¹Ù·Î°¡±â¿¡¼­ ¿Ê ÀÌ¹ÌÁö ¸µÅ©
       * °¡Á®¿À±â String productLinkResult = ""; if( productImgLinkRow != -1){
       * String productLinkRow = list.get(productLinkRowNum); //ex ) aaaaA<img
       * src = "dddddd">ddd String productLinkRow_Frist_Cut =
       * productLinkRow.substring(productImgLinkRow, productLinkRow.length());
       * //ex) <img src="ddddddd">ddd int productLinkRow_End_Cut_Index =
       * productLinkRow_Frist_Cut.indexOf(">"); //³¡°ª ½Äº° productLinkResult =
       * productLinkRow_Frist_Cut.substring(9, productLinkRow_End_Cut_Index);
       * // <img src="ddddddd"> }
       * 
       * 
       * System.out.println(productLinkResult); String hoho =
       * productLinkResult.replaceAll("[^°¡-ÆR]"," ");
       * System.out.println(hoho.trim());
       */

      String test = "<li id=\"anchorBoxId_65658\" class=\"item xans-record-\">";
      System.out.println(test.indexOf("<li id"));
      System.out.println(test.indexOf("xans-record"));

      Savve wr = new Savve();
      wr.writeFile();

   }
}