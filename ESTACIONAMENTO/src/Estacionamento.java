import java.io.*;
import java.util.*;

public class Estacionamento {

	char ativo;
	String codEst = "000001";
	String placa;
	String dataOperacao;
	char tipoOperacao;
	String modeloCor;
	String codMarca;
	String categoriaVeiculo;
	String horaEntrada;
	String horaSaida;
	float valorPago;
	String descricao;
	float valorTotal = 0;

	public long pesquisarVeiculo(String codigoPesq) {
		// metodo para localizar um registro ATIVO de ENTRADA no arquivo em disco
		long posicaoCursorArquivo = 0;
		try {
			RandomAccessFile arqEst = new RandomAccessFile("EST.DAT", "rw");
			while (true) {
				posicaoCursorArquivo = arqEst.getFilePointer();
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
				if (placa.equals(codigoPesq) && ativo == 'S' && tipoOperacao == 'E') {
					arqEst.close();
					return posicaoCursorArquivo;
				}
			}
		} catch (EOFException e) {
			return -1; // registro nao foi encontrado
		} catch (IOException e) {
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
			return -1;
		}
	}

	public void salvarVeiculo() {
		try {
			RandomAccessFile arqEst = new RandomAccessFile("EST.DAT", "rw");
			arqEst.seek(arqEst.length()); // posiciona o ponteiro no final do arquivo (EOF)
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
			arqEst.close();
			System.out.println("Dados gravados com sucesso !\n");
		} catch (IOException e) {
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
		}
	}

	public void excluirRegistro(long posicao) {
		// metodo para alterar o valor do campo ATIVO para N, tornando assim o registro excluido
		try {
			RandomAccessFile arqEst = new RandomAccessFile("EST.DAT", "rw");
			arqEst.seek(posicao);
			arqEst.writeChar('N'); // desativar o registro antigo
			arqEst.close();
		} catch (IOException e) {
			System.out.println("Erro na abertura do arquivo  -  programa sera finalizado");
			System.exit(0);
		}
	}

	// ***********************   INCLUSAO   *****************************
	public void incluir() {
		char confirmacao;
		int maiorPrimaryKey = 0;
		String codEstNew = "000001";
		try {
			RandomAccessFile arqEst = new RandomAccessFile("EST.DAT", "rw");
			while (true) {
				arqEst.readChar();
				String codLido = arqEst.readUTF();
				arqEst.readUTF();
				arqEst.readUTF();
				arqEst.readChar();
				arqEst.readUTF();
				arqEst.readUTF();
				arqEst.readUTF();
				arqEst.readUTF();
				arqEst.readUTF();
				arqEst.readFloat();
				if (Integer.parseInt(codLido) > maiorPrimaryKey) {
					maiorPrimaryKey = Integer.parseInt(codLido);
				}
			}
		} catch (EOFException a) {
			maiorPrimaryKey++;
			codEstNew = String.valueOf(maiorPrimaryKey);
			while (codEstNew.length() < 6) {
				codEstNew = "0" + codEstNew;
			}
		} catch (IOException j) {
			System.out.println("Erro na abertura do arquivo");
		}

		do {
			ativo = 'S';

			boolean valido = true;
			do {
				valido = true;
				System.out.print("Digite a placa do carro.........................: ");
				placa = Main.leia.nextLine();
				if (placa.equals("FIM")) {
					break;
				}
				if (placa.length() != 7) {
					System.out.println("Digite a placa com 7 caracteres, formato XXX9999");
					valido = false;
				} else {
					for (byte x = 0; x <= 6; x++) {
						if (x <= 2) {
							if (placa.charAt(x) < 'A' || placa.charAt(x) > 'Z') {
								System.out.println("Placa Invalida, digite letra maiuscula nas 3 primeiras posicoes");
								valido = false;
								break;
							}
						} else {
							if (placa.charAt(x) < '0' || placa.charAt(x) > '9') {
								System.out.println("Placa Invalida, digite numero nas 4 ultimas posicoes");
								valido = false;
								break;
							}
						}
					}
				}
			} while (!valido);

			if (placa.equals("FIM")) {
				break;
			}

			if (pesquisarVeiculo(placa) != -1) {
				System.out.println("Veiculo ja se encontra no estacionamento! Registre a saida antes de uma nova entrada.\n");
				continue;
			}

			codEst = codEstNew;

			do {
				System.out.print("Digite a data de operacao (DD/MM/AAAA)..........: ");
				dataOperacao = Main.leia.nextLine();
				if (!ValidarData(dataOperacao)) {
					System.out.println("Data invalida, digite novamente.");
				}
			} while (!ValidarData(dataOperacao));

			tipoOperacao = 'E';

			do {
				System.out.print("Digite o modelo/cor do carro (minimo 10 caracteres).............: ");
				modeloCor = Main.leia.nextLine();
				if (modeloCor.length() < 10) {
					System.out.println("Digite o modelo/cor do carro com pelo menos 10 caracteres.");
				}
			} while (modeloCor.length() < 10);

			int indiceMarca;
			do {
				System.out.print("Digite o codigo da Marca........................: ");
				codMarca = Main.leia.nextLine();
				indiceMarca = pesquisarMarcaVeiculo(codMarca);
				if (indiceMarca == -1) {
					System.out.println("Marca invalida, digite novamente.");
				} else {
					System.out.println("Marca: " + Main.vetDescricaoMarca[indiceMarca]);
				}
			} while (indiceMarca == -1);

			do {
				System.out.print("Digite a categoria do veiculo (GI/PI/GN/PN).....: ");
				categoriaVeiculo = Main.leia.nextLine();
				descricao = consistirCategoria(categoriaVeiculo);
				if (descricao.equals("ERRO")) {
					System.out.println("Categoria invalida, digite novamente.");
				} else {
					System.out.println("Categoria: " + descricao);
				}
			} while (descricao.equals("ERRO"));

			do {
				System.out.print("Digite o horario de entrada (HH:MM).............: ");
				horaEntrada = Main.leia.nextLine();
				if (!validarHora(horaEntrada)) {
					System.out.println("Hora invalida, digite novamente.");
				}
			} while (!validarHora(horaEntrada));

			horaSaida = "";
			valorPago = 0;

			do {
				System.out.print("\nConfirma a gravacao dos dados (S/N) ? ");
				confirmacao = Main.leia.next().charAt(0);
				Main.leia.nextLine();
				if (confirmacao == 'S') {
					System.out.println("O codigo do carro e: " + codEst);
					salvarVeiculo();
					maiorPrimaryKey++;
					codEstNew = String.valueOf(maiorPrimaryKey);
					while (codEstNew.length() < 6) {
						codEstNew = "0" + codEstNew;
					}
				}
			} while (confirmacao != 'S' && confirmacao != 'N');

		} while (!placa.equals("FIM"));
	}

	// ************************  SAIDA DE VEICULO (ALTERACAO)  *****************************
	public void alterar() {
		String codigo;
		char confirmacao;
		long posicaoRegistro = 0;

		do {
			do {
				System.out.println("\n ***************  SAIDA DE VEICULO ***************** ");
				System.out.print("Digite a placa do veiculo que deseja confirmar saida ( FIM para encerrar ): ");
				codigo = Main.leia.nextLine();
				if (codigo.equals("FIM")) {
					break;
				}
				posicaoRegistro = pesquisarVeiculo(codigo);
				if (posicaoRegistro == -1) {
					System.out.println("Veiculo nao encontrado ou saida ja registrada. Digite outro valor\n");
				}
			} while (posicaoRegistro == -1);

			if (codigo.equals("FIM")) {
				break;
			}

			System.out.println("Placa..............: " + placa);
			System.out.println("Data de entrada.....: " + dataOperacao);
			System.out.println("Modelo/Cor..........: " + modeloCor);
			System.out.println("Hora de entrada.....: " + horaEntrada);

			ativo = 'S';
			do {
				System.out.print("\nConfirma a saida do veiculo (S/N) ? ");
				confirmacao = Main.leia.next().charAt(0);
				Main.leia.nextLine();
				if (confirmacao == 'S') {
					excluirRegistro(posicaoRegistro);
					tipoOperacao = 'S';
					do {
						System.out.print("Digite a hora de saida (HH:MM): ");
						horaSaida = Main.leia.nextLine();
						if (!validarHora(horaSaida)) {
							System.out.println("Hora invalida, digite novamente.");
						} else if (horaSaida.compareTo(horaEntrada) <= 0) {
							System.out.println("Hora de saida deve ser maior que a hora de entrada (" + horaEntrada + ")!");
						}
					} while (!validarHora(horaSaida) || horaSaida.compareTo(horaEntrada) <= 0);

					valorPago = valorAPagar(categoriaVeiculo, horaEntrada, horaSaida);
					System.out.println("Valor a pagar: " + valorPago);
					salvarVeiculo();
				}
			} while (confirmacao != 'S' && confirmacao != 'N');

		} while (!codigo.equals("FIM"));
	}

	// ************************  EXCLUSAO  *****************************
	public void excluir() {
		String chave;
		char confirmacao;
		long posicaoRegistro = 0;

		do {
			do {
				System.out.println(" ***************  EXCLUSAO DE VEICULOS  ***************** ");
				System.out.print("Digite a placa do veiculo que deseja excluir ( FIM para encerrar ): ");
				chave = Main.leia.nextLine();
				if (chave.equals("FIM")) {
					break;
				}

				posicaoRegistro = pesquisarVeiculo(chave);
				if (posicaoRegistro == -1) {
					System.out.println("Veiculo nao encontrado ou sem entrada ativa. Digite outro valor\n");
				}
			} while (posicaoRegistro == -1);

			if (chave.equals("FIM")) {
				System.out.println("\n ************  ENCERRANDO EXCLUSAO  ************** \n");
				break;
			}

			System.out.println("placa veiculo.......: " + placa);
			System.out.println("Data de operacao....: " + dataOperacao);
			System.out.println("Modelo/Cor..........: " + modeloCor);
			System.out.println("Codigo da marca.....: " + codMarca);
			System.out.println("Categoria do veiculo: " + categoriaVeiculo);
			System.out.println("Horario de entrada..: " + horaEntrada);
			System.out.println("Horario de saida....: " + horaSaida);
			System.out.println("Tipo de operacao....: " + tipoOperacao);

			do {
				System.out.print("\nConfirma a exclusao deste veiculo (S/N) ? ");
				confirmacao = Main.leia.next().charAt(0);
				Main.leia.nextLine();
				if (confirmacao == 'S') {
					excluirRegistro(posicaoRegistro);
					System.out.println("Registro excluido com sucesso !\n");
				}
			} while (confirmacao != 'S' && confirmacao != 'N');

		} while (!chave.equals("FIM"));
	}

	// ************************  CONSULTA  *****************************
	public void consultar() {
		byte opcao;

		do {
			do {
				System.out.println("\n ***************  CONSULTA DE VEICULOS  ***************** ");
				System.out.println(" [1] CONSULTAR SOMENTE VEICULOS QUE NAO SAIRAM");
				System.out.println(" [2] LISTA DE TODOS OS VEICULOS ");
				System.out.println(" [3] LISTA SOMENTE OS REGISTROS EM UMA DATA ESPECIFICADA ");
				System.out.println(" [0] VOLTAR");
				System.out.print("\nDigite a opcao desejada: ");
				opcao = Main.leia.nextByte();
				Main.leia.nextLine();
				if (opcao < 0 || opcao > 3) {
					System.out.println("opcao Invalida, digite novamente.\n");
				}
			} while (opcao < 0 || opcao > 3);

			if (opcao == 0) break;

			switch (opcao) {
				case 1:
					consultarNaoSairam();
					break;
				case 2:
					consultarTodos();
					break;
				case 3:
					consultarPorData();
					break;
			}
		} while (opcao != 0);
	}

	public void consultarNaoSairam() {
		try {
			RandomAccessFile arqEst = new RandomAccessFile("EST.DAT", "rw");
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

				if (ativo == 'S' && tipoOperacao == 'E') {
					imprimirVeiculo();
				}
			}
		} catch (EOFException e) {
			System.out.println("\n FIM DA CONSULTA\n");
		} catch (IOException e) {
			System.out.println("Erro na leitura do arquivo.");
		}
	}

	public void consultarTodos() {
		try {
			RandomAccessFile arqEst = new RandomAccessFile("EST.DAT", "rw");
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
					imprimirVeiculo();
				}
			}
		} catch (EOFException e) {
			System.out.println("\n FIM DA CONSULTA\n");
		} catch (IOException e) {
			System.out.println("Erro na leitura do arquivo.");
		}
	}

	public void consultarPorData() {
		String dataConsultar;
		do {
			System.out.print("Digite a data a ser consultada (DD/MM/AAAA): ");
			dataConsultar = Main.leia.nextLine();
			if (!ValidarData(dataConsultar)) {
				System.out.println("Data invalida, digite novamente.");
			}
		} while (!ValidarData(dataConsultar));

		try {
			RandomAccessFile arqEst = new RandomAccessFile("EST.DAT", "rw");
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

				if (ativo == 'S' && dataOperacao.equals(dataConsultar)) {
					imprimirVeiculo();
				}
			}
		} catch (EOFException e) {
			System.out.println("\n FIM DA CONSULTA\n");
		} catch (IOException e) {
			System.out.println("Erro na leitura do arquivo.");
		}
	}

	public void imprimirCabecalho() {
		System.out.println("-CODIGO-  -PLACA-   -DATA-      -OP- -MODELO/COR-      -MARCA- -CATEGORIA- -HR.ENTR- -HR.SAI-  -VLR.PAGO-");
	}

	public void imprimirVeiculo() {
		System.out.println(
				formatarString(codEst, 8) + "  " +
				formatarString(placa, 8) + "  " +
				formatarString(dataOperacao, 10) + "  " +
				formatarString(String.valueOf(tipoOperacao), 4) + "  " +
				formatarString(modeloCor, 16) + "  " +
				formatarString(codMarca, 7) + "  " +
				formatarString(categoriaVeiculo, 10) + "  " +
				formatarString(horaEntrada, 8) + "  " +
				formatarString(horaSaida, 8) + "  " +
				valorPago);
	}

	public static String formatarString(String texto, int tamanho) {
		// retorna uma string com o numero de caracteres passado como parametro em TAMANHO
		if (texto.length() > tamanho) {
			texto = texto.substring(0, tamanho);
		} else {
			while (texto.length() < tamanho) {
				texto = texto + " ";
			}
		}
		return texto;
	}

	// ************************  RELATORIO DE FATURAMENTO  *****************************
	public void gerarRelatorio() {
		valorTotal = 0;
		System.out.print("Digite a placa do veiculo (ou deixe em branco para todos): ");
		String placaConsulta = Main.leia.nextLine();

		try {
			RandomAccessFile arqEst = new RandomAccessFile("EST.DAT", "rw");
			imprimirCabecalhoRelatorio();
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

				if (ativo == 'S' && tipoOperacao == 'S') {
					if (placaConsulta.equals("") || placa.equals(placaConsulta)) {
						valorTotal = valorTotal + valorPago;
						imprimirRelatorio();
					}
				}
			}
		} catch (EOFException e) {
			System.out.println("\nTOTAL FATURADO: " + String.format("%.2f", valorTotal) + "\n");
		} catch (IOException e) {
			System.out.println("Erro na leitura do arquivo.");
		}
	}

	public void imprimirRelatorio() {
		System.out.println(
				formatarString(placa, 8) + "  " +
				formatarString(modeloCor, 16) + "  " +
				formatarString(dataOperacao, 10) + "  " +
				formatarString(horaEntrada, 8) + "  " +
				formatarString(horaSaida, 8) + "  " +
				String.format("%.2f", valorPago));
	}

	public void imprimirCabecalhoRelatorio() {
		System.out.println("-PLACA-   -MODELO/COR-      -DATA-      -HR.ENTRADA- -HR.SAIDA- -VLR.PAGO-");
	}

	// ************************  VALIDACOES  *****************************
	public static boolean ValidarData(String data) {
		if (data.length() != 10 || data.charAt(2) != '/' || data.charAt(5) != '/') {
			System.out.println("Data invalida, digite no formato (DD/MM/AAAA)!");
			return false;
		}
		try {
			int dia = Integer.parseInt(data.substring(0, 2));
			int mes = Integer.parseInt(data.substring(3, 5));
			int ano = Integer.parseInt(data.substring(6, 10));
			if (dia < 1 || dia > 31) {
				System.out.println("Data invalida, dia fora do intervalo!");
				return false;
			}
			if (mes < 1 || mes > 12) {
				System.out.println("Data invalida, mes fora do intervalo!");
				return false;
			}
			if (ano < 1900 || ano > 2100) {
				System.out.println("Data invalida, ano fora do intervalo!");
				return false;
			}
		} catch (NumberFormatException e) {
			System.out.println("Data invalida, digite no formato (DD/MM/AAAA)!");
			return false;
		}
		return true;
	}

	public static boolean validarHora(String hora) {
		if (hora.length() != 5 || hora.charAt(2) != ':') {
			System.out.println("Hora invalida, digite no formato (HH:MM)!");
			return false;
		}
		try {
			int horaInt = Integer.parseInt(hora.substring(0, 2));
			int minutoInt = Integer.parseInt(hora.substring(3, 5));
			if (horaInt < 0 || horaInt > 23) {
				System.out.println("Hora invalida, hora fora do intervalo (0-23)!");
				return false;
			}
			if (minutoInt < 0 || minutoInt > 59) {
				System.out.println("Hora invalida, minuto fora do intervalo (0-59)!");
				return false;
			}
		} catch (NumberFormatException e) {
			System.out.println("Hora invalida, digite no formato (HH:MM)!");
			return false;
		}
		return true;
	}

	/**
	 * Consiste a categoria do veiculo.
	 * Retorna a descricao da categoria se for valida, ou "ERRO" caso contrario.
	 */
	public static String consistirCategoria(String categoria) {
		if (categoria.equals("GI")) {
			return "Grande e Importado";
		} else if (categoria.equals("PI")) {
			return "Pequeno e Importado";
		} else if (categoria.equals("GN")) {
			return "Grande e Nacional";
		} else if (categoria.equals("PN")) {
			return "Pequeno e Nacional";
		} else {
			return "ERRO";
		}
	}

	/**
	 * Calcula o valor a pagar pela permanencia do veiculo no estacionamento.
	 * Formula: ( HS - HE + (MS - ME) / 60 ) * VlrHora
	 */
	public static float valorAPagar(String categoria, String horaEntrada, String horaSaida) {
		int horaEntradaInt = Integer.parseInt(horaEntrada.substring(0, 2));
		int horaSaidaInt = Integer.parseInt(horaSaida.substring(0, 2));
		int minutoEntradaInt = Integer.parseInt(horaEntrada.substring(3, 5));
		int minutoSaidaInt = Integer.parseInt(horaSaida.substring(3, 5));
		float vlrHora;

		boolean importado = categoria.equals("GI") || categoria.equals("PI");
		boolean grande = categoria.equals("GI") || categoria.equals("GN");

		if (horaEntradaInt <= 18) {
			// entrada até as 18:00
			if (importado) {
				vlrHora = grande ? 10.00f : 8.20f;
			} else {
				vlrHora = grande ? 9.00f : 7.00f;
			}
		} else {
			// entrada depois das 18:00
			if (importado) {
				vlrHora = grande ? 8.00f : 6.50f;
			} else {
				vlrHora = grande ? 7.50f : 6.00f;
			}
		}

		float totalHoras = (horaSaidaInt - horaEntradaInt) + (minutoSaidaInt - minutoEntradaInt) / 60f;
		return totalHoras * vlrHora;
	}

	/**
	 * Pesquisa um codigo de marca no vetor vetCodMarca (declarado em Main.java).
	 * Retorna o indice se encontrado, ou -1 caso contrario.
	 */
	public static int pesquisarMarcaVeiculo(String codigoMarca) {
		for (int i = 0; i < Main.vetCodMarca.length; i++) {
			if (Main.vetCodMarca[i].equalsIgnoreCase(codigoMarca)) {
				return i;
			}
		}
		return -1;
	}
}