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

   String frontURL = "http://www.66girls.co.kr/product/search.html?banner_action=&keyword=��Ʈ"; //��������
   //String frontURL = "http://www.michyeora.com/shop/search.php?keyword=%EC%BD%94%ED%8A%B8"; //���Ķ� ��Ʈ %C4%DA%C6%AE& //�ҳ೪���� ��Ʈ
  // String frontURL = "http://ggsing.com/product/search.html?banner_action=&keyword=��Ʈ";//����
      
   //String frontURL = "http://imvely.com/product/search.html?view_type=&supplier_code=&category_no=&search_type=product_name&keyword=��Ʈ"; // �Ӻ�
   // String searchURL = "��Ʈ"; //�˻��Ҵܾ�
   // String endURL = "&page=1";
   String abc = frontURL;

   List<String> list = new ArrayList<String>();
   List<Integer> startLiRowNum = new ArrayList<Integer>(); // ��ǰ ���� box �� ����
   List<Integer> endLiRowNum = new ArrayList<Integer>(); // ��ǰ �� box �� ����
   

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
         System.out.println("�Ϸ�");
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         // ��ǰ �ϳ� ���� �±� �ν�
         String startLI = "<li id";
         String startClass = "xans-record"; // ���θ� ��ǰ �ڽ� class �̸�

         // ��ǰ �ϳ� �� �±� �ν�
         String endLI = "</li>"; // �̰� row ��ü���� </li>�̿��� �Ѵ�.

         // ��ǰ �ϳ� �ڽ� ����

         // ��ǰ �ٷΰ��� ��ũ ������ �ν�
         String productLink[] = { "a href=\"/product/detail", "a href=\"/shop/view" };
         String product_NameCheck = "[��-�R]"; // ��ǰ �̹��� �˻� (�ѱ� ������ true ����)
         int productImgLinkRowNum = 0;// �̹��� src ����ִ� �� �˻���

         String Not_product_NameCheck = "[^��-�R]"; // ��ǰ �̸� ���� �ִ���� 10, ��ǰ �ݾ���
                                          // �ѱ� 4���� ����, ��ǰ ������ �ѱ�
                                          // 10�� ���� �ѱ� �˻� ������
                                          // false����
         int productLinkRowNum = 0; // ����� ����

         for (int i = 0; i < list.size(); i++) {
            String row = list.get(i);
            int startlicheck = -1;
            int startliclasscheck = -1;
            startlicheck = row.indexOf(startLI);
            startliclasscheck = row.indexOf(startClass);

            if (startlicheck != -1 && startliclasscheck != -1) {
               startLiRowNum.add(i);
               System.out.println("startBoxRow �߰��Ϸ�");
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
               
               //���� ��ũ��
               //���� ����
               //���� �̸���
               //���� �ݾ� ��
               String ImageLink = "";//���� �̹��� ��ũ��
               
               
               for(int p=0; p<productLink.length; p++){
                  int pLinkNum = row.indexOf(productLink[p]);
//                   System.out.println(row.replaceAll("[^��-�R]","").length() + "////" + row);
                  
                  if( pLinkNum != -1 && row.replaceAll("[^��-�R]","").length() > 0){ //�� �ٿ� ��ũ�� �ְ� �ѱ��� ������
                     //��ũ �� ����
                     //�ѱ� �� ����
                     System.out.println(row);
                     break;
                  }else if(pLinkNum != -1 &&  row.replaceAll("[^��-�R]","").length() == 0){ //���࿡ ��ũ�� �ְ� �ѱ��� ������ �̹��� ��ũ�� ������ �̹��� ��ũ ����
                     System.out.println(j);
                     ImageLink= searchImgLink(row);      
                     System.out.println(ImageLink);
                  }
               }
               
               
            }
            
         }

      }   
   }
   
    //��ǰ �ٷΰ��� ��ũ ������ �ν��ϸ� �̹�����ũ ����
    public String searchImgLink(String row){
        
       String pattern[] = {".gif",".jpg",".png"};
       
        int productImgLinkRow = row.indexOf("<img src");        //��ǰ �ٷΰ��⿡�� �� �̹��� ��ũ �ٹ�ȣ ��������

        String productLinkResult = "";        //��ǰ �ٷΰ��⿡�� �� �̹��� ��ũ ��������
        if( productImgLinkRow != -1){
           String productLinkRow = row; //ex ) aaaaA<img src = "dddddd">ddd
           String productLinkRow_Frist_Cut = productLinkRow.substring(productImgLinkRow, productLinkRow.length()); //ex) <img src="ddddddd">ddd
           int productLinkRow_End_Cut_Index = -1;
           for(int i=0; i<pattern.length; i++){
              if( productLinkRow_Frist_Cut.indexOf(pattern[i]) != -1){
              productLinkRow_End_Cut_Index = productLinkRow_Frist_Cut.indexOf(pattern[i])+5; //���� �ĺ� .gif .png .jpg
              }
           }
           productLinkResult = productLinkRow_Frist_Cut.substring(10, productLinkRow_End_Cut_Index-1); // <img src="ddddddd">
        }
        return productLinkResult;
    }
    
   public static void main(String args[]) {
      /*
       * List<String> list = new ArrayList<String>(); list.
       * add("adfsdfsdafasdf<img src=\"ddhdhdhdh.dh�̰����ѱ��Դϴ�dh\">dd�̰����ѱ��Դϴ�dddddd<src <img <src img ><src img <img src > >dd"
       * ); int productLinkRowNum = 0; //����� ���� //��ǰ �ٷΰ��⿡�� �� �̹��� ��ũ �ٹ�ȣ ��������
       * int productImgLinkRow =
       * list.get(productLinkRowNum).indexOf("<img src"); //��ǰ �ٷΰ��⿡�� �� �̹��� ��ũ
       * �������� String productLinkResult = ""; if( productImgLinkRow != -1){
       * String productLinkRow = list.get(productLinkRowNum); //ex ) aaaaA<img
       * src = "dddddd">ddd String productLinkRow_Frist_Cut =
       * productLinkRow.substring(productImgLinkRow, productLinkRow.length());
       * //ex) <img src="ddddddd">ddd int productLinkRow_End_Cut_Index =
       * productLinkRow_Frist_Cut.indexOf(">"); //���� �ĺ� productLinkResult =
       * productLinkRow_Frist_Cut.substring(9, productLinkRow_End_Cut_Index);
       * // <img src="ddddddd"> }
       * 
       * 
       * System.out.println(productLinkResult); String hoho =
       * productLinkResult.replaceAll("[^��-�R]"," ");
       * System.out.println(hoho.trim());
       */

      String test = "<li id=\"anchorBoxId_65658\" class=\"item xans-record-\">";
      System.out.println(test.indexOf("<li id"));
      System.out.println(test.indexOf("xans-record"));

      Savve wr = new Savve();
      wr.writeFile();

   }
}