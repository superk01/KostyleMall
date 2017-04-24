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

   String frontURL = "http://www.66girls.co.kr/product/search.html?banner_action=&keyword=코트"; //육육걸즈
   //String frontURL = "http://www.michyeora.com/shop/search.php?keyword=%EC%BD%94%ED%8A%B8"; //미쳐라 코트 %C4%DA%C6%AE& //소녀나라의 코트
  // String frontURL = "http://ggsing.com/product/search.html?banner_action=&keyword=코트";//고고싱
      
   //String frontURL = "http://imvely.com/product/search.html?view_type=&supplier_code=&category_no=&search_type=product_name&keyword=코트"; // 임블리
   // String searchURL = "코트"; //검색할단어
   // String endURL = "&page=1";
   String abc = frontURL;

   List<String> list = new ArrayList<String>();
   List<Integer> startLiRowNum = new ArrayList<Integer>(); // 물품 시작 box 줄 모음
   List<Integer> endLiRowNum = new ArrayList<Integer>(); // 물품 끝 box 줄 모음
   

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
         System.out.println("완료");
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         // 물품 하나 시작 태그 인식
         String startLI = "<li id";
         String startClass = "xans-record"; // 쇼핑몰 물품 박스 class 이름

         // 물품 하나 끝 태그 인식
         String endLI = "</li>"; // 이건 row 전체값이 </li>이여야 한다.

         // 물품 하나 박스 생성

         // 물품 바로가기 링크 페이지 인식
         String productLink[] = { "a href=\"/product/detail", "a href=\"/shop/view" };
         String product_NameCheck = "[가-힣]"; // 물품 이미지 검색 (한글 있으면 true 리턴)
         int productImgLinkRowNum = 0;// 이미지 src 담겨있는 열 검사결과

         String Not_product_NameCheck = "[^가-힣]"; // 물품 이름 추출 최대길이 10, 물품 금액은
                                          // 한글 4글자 이하, 물품 설명은 한글
                                          // 10자 이하 한글 검사 있으면
                                          // false리턴
         int productLinkRowNum = 0; // 결과값 저장

         for (int i = 0; i < list.size(); i++) {
            String row = list.get(i);
            int startlicheck = -1;
            int startliclasscheck = -1;
            startlicheck = row.indexOf(startLI);
            startliclasscheck = row.indexOf(startClass);

            if (startlicheck != -1 && startliclasscheck != -1) {
               startLiRowNum.add(i);
               System.out.println("startBoxRow 추가완료");
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
               
               //물건 링크값
               //물건 설명값
               //물건 이름값
               //물건 금액 값
               String ImageLink = "";//물건 이미지 링크값
               
               
               for(int p=0; p<productLink.length; p++){
                  int pLinkNum = row.indexOf(productLink[p]);
//                   System.out.println(row.replaceAll("[^가-힣]","").length() + "////" + row);
                  
                  if( pLinkNum != -1 && row.replaceAll("[^가-힣]","").length() > 0){ //이 줄에 링크가 있고 한글이 있으면
                     //링크 값 추출
                     //한글 값 추출
                     System.out.println(row);
                     break;
                  }else if(pLinkNum != -1 &&  row.replaceAll("[^가-힣]","").length() == 0){ //만약에 링크만 있고 한글이 없으면 이미지 링크도 있으니 이미지 링크 추출
                     System.out.println(j);
                     ImageLink= searchImgLink(row);      
                     System.out.println(ImageLink);
                  }
               }
               
               
            }
            
         }

      }   
   }
   
    //물품 바로가기 링크 페이지 인식하면 이미지링크 추출
    public String searchImgLink(String row){
        
       String pattern[] = {".gif",".jpg",".png"};
       
        int productImgLinkRow = row.indexOf("<img src");        //물품 바로가기에서 옷 이미지 링크 줄번호 가져오기

        String productLinkResult = "";        //물품 바로가기에서 옷 이미지 링크 가져오기
        if( productImgLinkRow != -1){
           String productLinkRow = row; //ex ) aaaaA<img src = "dddddd">ddd
           String productLinkRow_Frist_Cut = productLinkRow.substring(productImgLinkRow, productLinkRow.length()); //ex) <img src="ddddddd">ddd
           int productLinkRow_End_Cut_Index = -1;
           for(int i=0; i<pattern.length; i++){
              if( productLinkRow_Frist_Cut.indexOf(pattern[i]) != -1){
              productLinkRow_End_Cut_Index = productLinkRow_Frist_Cut.indexOf(pattern[i])+5; //끝값 식별 .gif .png .jpg
              }
           }
           productLinkResult = productLinkRow_Frist_Cut.substring(10, productLinkRow_End_Cut_Index-1); // <img src="ddddddd">
        }
        return productLinkResult;
    }
    
   public static void main(String args[]) {
      /*
       * List<String> list = new ArrayList<String>(); list.
       * add("adfsdfsdafasdf<img src=\"ddhdhdhdh.dh이것은한글입니다dh\">dd이것은한글입니다dddddd<src <img <src img ><src img <img src > >dd"
       * ); int productLinkRowNum = 0; //결과값 저장 //물품 바로가기에서 옷 이미지 링크 줄번호 가져오기
       * int productImgLinkRow =
       * list.get(productLinkRowNum).indexOf("<img src"); //물품 바로가기에서 옷 이미지 링크
       * 가져오기 String productLinkResult = ""; if( productImgLinkRow != -1){
       * String productLinkRow = list.get(productLinkRowNum); //ex ) aaaaA<img
       * src = "dddddd">ddd String productLinkRow_Frist_Cut =
       * productLinkRow.substring(productImgLinkRow, productLinkRow.length());
       * //ex) <img src="ddddddd">ddd int productLinkRow_End_Cut_Index =
       * productLinkRow_Frist_Cut.indexOf(">"); //끝값 식별 productLinkResult =
       * productLinkRow_Frist_Cut.substring(9, productLinkRow_End_Cut_Index);
       * // <img src="ddddddd"> }
       * 
       * 
       * System.out.println(productLinkResult); String hoho =
       * productLinkResult.replaceAll("[^가-힣]"," ");
       * System.out.println(hoho.trim());
       */

      String test = "<li id=\"anchorBoxId_65658\" class=\"item xans-record-\">";
      System.out.println(test.indexOf("<li id"));
      System.out.println(test.indexOf("xans-record"));

      Savve wr = new Savve();
      wr.writeFile();

   }
}