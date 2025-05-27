package visao;
import other.Input;

public class Launch {
    public static void main(String[] args){
        new ViewTLB();
        new ViewTabelaPaginas();
        
        {// teste
        	try {
				Input input = new Input("C:/teste/teste.txt");
				for(int i = 0; i<input.getInstrucoes().size(); i++) {
					System.out.println(input.getInstrucoes().get(i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
        	
        }
        
    }
}
