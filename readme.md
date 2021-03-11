# Renomeador de Boletos

## Objetivo
Aplicação para auxiliar no processo de dividir e renomear boletos em pdf.

Ao gerar boletos de cobrança de clientes, é comum que o banco gere **um único arquivo PDF com todos os boletos de todos 
os clientes**.

Esse programa lê um arquivo pdf contendo vários boletos de vários clientes e o separa, salvando cada arquivo pdf com o 
nome do cliente correspondente.

> Atenção: Funciona apenas para os bancos **Itaú** e **Santander**!

## Funcionamento
Procura por exatamente um arquivo .pdf localizado no mesmo diretório que o jar e o separa por página, salvando cada página
 individualmente e nomeando-a de acordo com o pagador identificado.

## Instruções
> Requer o Java 8 ou superior instalado!

Copie o **jar** para uma pasta que contenha apenas **um único arquivo pdf** e execute-o. Os boletos serão salvos nesse 
mesmo diretório.  
