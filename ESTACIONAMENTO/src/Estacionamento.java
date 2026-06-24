import java.io.*;
import java.util.*;

public class Estacionamento {

	char ativo; 
	String codEst = "000001"; 
	String placa; 
	String dataOperacao; 
	char tipoOperacao;
	String modeloCor;
	String codMarca ;
	String categoriaVeiculo; 
	String horaEntrada;
	String horaSaida;
	float valorPago;

	public long pesquisarVeiculo(String codigoPesq) {	
		// metodo para localizar um registro no arquivo em disco
		long posicaoCursorArquivo = 0;
		try { 
			RandomAccessFile arqEst = new RandomAccessFile("EST.DAT", "rw");
			while (true) {
				posicaoCursorArquivo  = arqEst.getFilePointer();
				ativo = arqEst.readChar(); 
				 codEst = arqEst.readUTF(); 
				 placa = arqEst.readUTF();
				 dataOperacao = arqEst.readUTF();
				 tipoOperacao =  arqEst.readChar();
				 modeloCor = arqEst.readUTF();
				 codMarca = arqEst.readUTF();
				 categoriaVeiculo = arqEst.readUTF();
				 horaEntrada = arqEst.readUTF();
				 horaSaida = arqEst.readUTF();
				 valorPago = arqEst.readFloat();
				if ( placa.equals(codigoPesq) && ativo == 'S') {
					arqEst.close();
					return posicaoCursorArquivo;
				}
			}
		}catch (EOFException e) {
			return -1; // registro nao foi encontrado
		}catch (IOException e) {
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
			return -1;
		}
	}

	public void salvarVeiculo() {
		try {
			RandomAccessFile arqEst = new RandomAccessFile("EST.DAT", "rw");
			long posicaoCursorArquivo = arqEst.getFilePointer();	
			arqEst.seek(arqEst.length());  // posiciona o ponteiro no final do arquivo (EOF)
			arqEst.writeChar(ativo); 
			arqEst.writeUTF(codEst); 
			arqEst.writeUTF(placa);
			arqEst.writeUTF(dataOperacao);
			arqEst.writeChar(tipoOperacao);
			arqEst.writeUTF(modeloCor);
			arqEst.writeUTF(codMarca);
			arqEst.writeUTF(categoriaVeiculo);
			arqEst.writeUTF(horaEntrada);
			arqEst.writeUTF(horaSaida);
			arqEst.writeFloat(valorPago);
			System.out.println("Dados gravados com sucesso !\n");
		}catch (IOException e) { 
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
		}
	}

	public void excluirRegistro(long posicao)	{    
		// metodo para alterar o valor do campo ATIVO para N, tornando assim o registro excluido
		try {
			RandomAccessFile arqEst = new RandomAccessFile("EST.DAT", "rw");			
			arqEst.seek(posicao);
			arqEst.writeChar('N');   // desativar o registro antigo
			arqEst.close();
		}catch (IOException e) { 
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
		}
	}

	// ***********************   INCLUSAO   *****************************
	/**
	 * 
	 */
	public void incluir() {
		char confirmacao;
		char novoTipo = 'E';
		int maiorPrimaryKey = 0;
		String codEstNew = "";
		try {
			RandomAccessFile arqEst = new RandomAccessFile("EST.DAT", "rw");
			while (true) {
				ativo = arqEst.readChar(); 
				codEst = arqEst.readUTF(); 
				placa = arqEst.readUTF();
				dataOperacao = arqEst.readUTF();
				tipoOperacao =  arqEst.readChar();
				modeloCor = arqEst.readUTF();
				codMarca = arqEst.readUTF();
				categoriaVeiculo = arqEst.readUTF();
				horaEntrada = arqEst.readUTF();
				horaSaida = arqEst.readUTF();
				valorPago = arqEst.readFloat();
				if (Integer.parseInt(codEst) > maiorPrimaryKey) {
					maiorPrimaryKey = Integer.parseInt(codEst);
				}
			}
		} catch (EOFException a) {
			if (maiorPrimaryKey > 0) {
				maiorPrimaryKey ++;
				codEstNew = String.valueOf(maiorPrimaryKey);
				while(codEstNew.length() < 6) {
					codEstNew = "0" + codEstNew;
				}
			}
		} catch(IOException j) {
			System.out.println("Erro na arbetura do arquivo");
		}
		do {
			ativo = 'S';
			
			boolean valido = true;
			do {
				System.out.print("Digite a placa do carro.........................: ");
				placa = Main.leia.nextLine();
				if(placa.equals("FIM")) {
					break;
				}
				if (placa.length() != 7) {
					System.out.println("Digite a placa com 7 caracteres, "
							+ "formato XXX9999");
					valido = false;
				} else {
					for (byte x = 0; x <= 6; x ++) {
						if (x <= 2) {
							if (placa.charAt(x) < 'A' || placa.charAt(x) > 'Z') {
								System.out.println("Placa Inválida, digite letra "
										+ "maiúscula nas 3 primeiras posições");
								valido = false;
								break;
							}
						} else {
							if (placa.charAt(x) < '0' || placa.charAt(x) > '9') {
								System.out.println("Placa Inválida, digite número "
										+ "nas 4 últimas posições");
								valido = false;
								break;
							}
						}
					}
				}
			}while( ! valido );
			if(placa.equals("FIM")) {
				break;
			}
			codEst = codEstNew;
			do {
				System.out.print("Digite a data de operação (DD/MM/AAAA)..........: ");
				dataOperacao = Main.leia.nextLine();
				if (dataOperacao.charAt(3) != '/' || dataOperacao.charAt(6) != '/') {
					System.out.println("Data invalida, digite no formato (DD/MM/AAAA)!");
				}
				
			} while(true);
			
			tipoOperacao = 'E';
			System.out.print("Digite a cor modelo do carro....................: ");
			modeloCor = Main.leia.nextLine();
			System.out.print("Digite o codigo da Marca........................: ");
			codMarca = Main.leia.nextLine();
			System.out.print("Digite a categoria do veiculo...................: ");
			categoriaVeiculo = Main.leia.nextLine();
			System.out.print("Digite o horario de entrada.....................: ");
			horaEntrada = Main.leia.nextLine();
			horaSaida = "HH:MM";
			valorPago = 0;
			do {
				System.out.print("\nConfirma a gravacao dos dados (S/N) ? ");
				confirmacao = Main.leia.next().charAt(0);
				if (confirmacao == 'S') {
					System.out.println("O codigo do carro e:" + codEst);
					salvarVeiculo();
				}
			}while (confirmacao != 'S' && confirmacao != 'N');
		}while ( ! placa.equals("FIM"));
	}
	//************************  ALTERACAO  *****************************
	public void alterar() {
		String codigo;
		char confirmacao;
		long posicaoRegistro = 0;

		do {
			do {
				Main.leia.nextLine();
				System.out.println("\n ***************  SAIDA E ENTRADA DE VEICULO ***************** ");
				System.out.print("Digite a Placa do veiculo que deseja confirmar saida ( FIM para encerrar ): ");
				codigo = Main.leia.nextLine();
				if (codigo.equals("FIM")) {
					break;
				}
				posicaoRegistro = pesquisarVeiculo(codigo);
				if (posicaoRegistro == -1) {
					System.out.println("Placa nao cadastrada no arquivo, digite outro valor\n");
				}
			}while (posicaoRegistro == -1);

			if (codigo.equals("FIM")) {
				break;
			}

			ativo = 'S';
			do {
				System.out.print("\nConfirma a saida do veiculo (S/N) ? ");
				confirmacao = Main.leia.next().charAt(0);
				if (confirmacao == 'S') {
					excluirRegistro(posicaoRegistro);
					tipoOperacao = 'S';
					System.out.println("Digite a hora de saida: ");
					horaSaida = Main.leia.nextLine();
					salvarVeiculo();
					System.out.println("Dados gravados com sucesso !\n");
				}
			}while (confirmacao != 'S' && confirmacao != 'N');

		}while ( ! codigo.equals("FIM"));
	}

	//************************  EXCLUSAO  *****************************
	public void excluir() {
		String matriculaChave;
		char confirmacao;
		long posicaoRegistro = 0;

		do {
			do {
				System.out.println(" ***************  EXCLUSAO DE ALUNOS  ***************** ");
				System.out.print("Digite a placa do veiculo que deseja excluir( FIM para encerrar ): ");
				matriculaChave = Main.leia.nextLine();
				if (matriculaChave.equals("FIM")) {
					break;
				}

				posicaoRegistro = pesquisarVeiculo(matriculaChave);
				if (posicaoRegistro == -1) {
					System.out.println("Matricula nao cadastrada no arquivo, digite outro valor\n");
				}
			}while (posicaoRegistro == -1);

			if (matriculaChave.equals("FIM")) {
				System.out.println("\n ************  PROGRAMA ENCERRADO  ************** \n");
				break;
			}

			System.out.println("placa veiculo.......: " + placa);
			System.out.println("Data de operação....: " + dataOperacao);
			System.out.println("Cor do modelo.......: " + modeloCor);
			System.out.println("codigo da marca.....: " + codMarca);
			System.out.println("categoria do veiculo: " + categoriaVeiculo);
			System.out.println("horario de entrada..: " + horaEntrada);
			System.out.println("horario de saida....: " + horaSaida);
			System.out.println("Tipo de operação....: " + tipoOperacao);

			do {
				System.out.print("\nConfirma a exclusao deste veiculo (S/N) ? ");
				confirmacao = Main.leia.next().charAt(0);
				if (confirmacao == 'S') {
					excluirRegistro(posicaoRegistro);
					System.out.println("Registro excluido com sucesso !\n");
				}
			}while (confirmacao != 'S' && confirmacao != 'N');

		}while ( ! matriculaChave.equals("FIM"));
	}

	//************************  CONSULTA  *****************************
	public void consultar() 	{
		RandomAccessFile arqEst;
		byte opcao;

		do {
			do {
				System.out.println("\n ***************  CONSULTA DE VEICULOS  ***************** ");
				System.out.println(" [1] CONSULTAR SOMENTO VEICULOS QUE NAO SAIRAM");
				System.out.println(" [2] LISTA DE TODOS OS VEICULOS ");
				System.out.println(" [3] LISTA SOMENTE OS REGISTROS EM UMA DATA ESPECIFICADA ");
				System.out.println(" [0] SAIR");
				System.out.print("\nDigite a opcao desejada: ");
				opcao = Main.leia.nextByte();
				if (opcao < 0 || opcao > 3) {
					System.out.println("opcao Invalida, digite novamente.\n");
				}
			}while (opcao < 0 || opcao > 3);

			if (opcao == 0) break;

			try {
				arqEst = new RandomAccessFile("EST.DAT", "rw");
				imprimirCabecalho();
				while (true) {
					ativo = arqEst.readChar();
					codEst = arqEst.readUTF();
					placa = arqEst.readUTF();
					dataOperacao = arqEst.readUTF();
					tipoOperacao = arqEst.readChar();
					modeloCor = arqEst.readUTF();
					codMarca = arqEst.readUTF();
					categoriaVeiculo = arqEst.readUTF();
					horaEntrada = arqEst.readUTF();
					horaSaida = arqEst.readUTF();
					valorPago = arqEst.readFloat();

					if (ativo == 'S') {
						if (opcao == 1 && tipoOperacao == 'E') {
							imprimirVeiculo();
						} else if (opcao == 2) {
							imprimirVeiculo();
						}
					}
				}
			} catch (EOFException e) {
				System.out.println("\n FIM DE RELATORIO - ENTER para continuar...\n");
				Main.leia.nextLine(); // Limpa buffer
				Main.leia.nextLine(); // Aguarda Enter
			} catch (IOException e) {
				System.out.println("Erro na leitura do arquivo.");
			}
		} while (opcao != 0);
	}

	public void imprimirCabecalho () {
		System.out.println("-ID-      -PLACA-   -DATA-      -OP- -MODELO/COR-");
	}

	public void imprimirVeiculo () {
		System.out.println(	formatarString(codEst, 8 ) + "  " +
				formatarString(placa , 8) + "  " +
				formatarString(dataOperacao , 10) + "  " +
				formatarString( String.valueOf(tipoOperacao) , 4 ) + "  " +
				formatarString( modeloCor , 15 )   );
	}

	public static String formatarString (String texto, int tamanho) {
		// retorna uma string com o numero de caracteres passado como parametro em TAMANHO
		if (texto.length() > tamanho) {
			texto = texto.substring(0,tamanho);
		}else{
			while (texto.length() < tamanho) {
				texto = texto + " ";
			}
		}
		return texto;
	}
}
