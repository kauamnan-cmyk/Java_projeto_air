import java.util.*;
public class Main {
	static Scanner leia = new Scanner(System.in);
	
	public static void main(String[] args) {	
		Estacionamento estacionamento = new Estacionamento();
    	byte opcao = -1;
    	 
    	do {
			do {
    			System.out.println("\n ***************  CADASTRO DE ALUNOS  ***************** ");
    			System.out.println(" [0] RELATORIO DE FATURAMENTO");
				System.out.println(" [1] INCLUIR ENTRADA DE VEICULO ");
    			System.out.println(" [2] SAIDA DE VEICULO ");
    			System.out.println(" [3] CONSULTAR DE VEICULOs ");
    			System.out.println(" [4] EXCLUIR VEICULOS ");
				System.out.println(" [5] SAIR ");
    			System.out.print("\nDigite a opcao desejada: ");
    			opcao = leia.nextByte();
    			leia.nextLine();
    			if (opcao < 0 || opcao > 5) {
    				System.out.println("opcao Invalida, digite novamente.\n");
    			}
    		}while (opcao < 0 || opcao > 5);
			
			switch (opcao) {
				case 0:
					estacionamento.gerarRelatorio();
					break;
				case 1: 
					estacionamento.incluir(); 
					break;
				case 2:
					estacionamento.alterar();
					break;
				case 3: 
					estacionamento.consultar();
					break;
				case 4: 
					estacionamento.excluir();
					break;
				case 5: 
					System.out.println("\n ************  PROGRAMA ENCERRADO  ************** \n");
					break;
			}
    	} while ( opcao != 5 );
    	leia.close();	}

		public static String[] vetCodMarca = {
		    "BM", "VW", "FO", "MB", "CV",
		    "FI", "AU", "TO", "HO", "HY"
		};

		public static String[] vetDescricaoMarca = {
		    "BMW", "VOLKSWAGEN", "FIAT", "MERCEDES", "CHEVROLET",
		    "FERRARI", "AUDI", "TOYOTA", "HONDA", "HYUNDAI"
		};

}
