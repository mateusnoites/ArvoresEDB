# Árvores balanceadas: AVL e Rubro-Negra (LLRB)

Trabalho da Unidade II de EDB 2.

**Autor:** Mateus Dias Oliveira - 20250034167

Programa em Java para registrar, localizar e remover identificadores de encomendas.

- **AVL**: balanceamento por altura e rotações.
- **Rubro-negra caída para a esquerda (LLRB)**: balanceamento por cores e rotações.

## Como compilar e executar
```
javac -d bin *.java
java -cp bin Main
java -cp bin Testes
```

## Funcionalidades
No início, o programa pergunta qual árvore usar. Em cada árvore:

| Opção | Ação |
|---|---|
| 1 | Inserir elemento (valores repetidos são ignorados) |
| 2 | Remover elemento (valores inexistentes são informados) |
| 3 | Buscar elemento |
| 4 | Percurso em ordem |
| 5 | Visualizar a estrutura (na LLRB, `(V)` = vermelho e `(P)` = preto) |
| 6 | Estatísticas: altura, rotações, inversões de cor e validação das propriedades |
| 0 | Voltar |

Entradas que não sejam números inteiros são recusadas com um aviso.

### Exemplo de uso (LLRB)
Inserindo 10, 20, 30, 40, 50 e 25, a opção 5 mostra:
```
    50 (P)
40 (P)
        30 (P)
            25 (V)
    20 (V)
        10 (P)
```
A árvore é impressa deitada, sendo que a subárvore direita fica acima da raíz e a subárvore esquerda fica abaixo.

## Testes
`java -cp bin Testes` executa 42 verificações e uma comparação entre as árvores, usando as mesmas sequências nas duas:

- árvore vazia e com um elemento;
- busca de elementos existentes e inexistentes;
- inserção de valores repetidos;
- remoção de folha, de nó com um filho, de nó com dois filhos, da raiz e de elementos inexistentes;
- inserção em ordem crescente, decrescente e aleatória (1000 elementos), validando as propriedades após cada inserção;
- 20000 operações aleatórias comparadas com `java.util.TreeSet`, validando as propriedades a cada operação;
- tabela de altura, rotações e inversões de cor para n = 1000, 10000 e 100000.

## Arquivos
| Arquivo | Conteúdo |
|---|---|
| `Arvore.java` | Interface comum |
| `AVL.java` | Árvore AVL |
| `LLRB.java` | Árvore rubro-negra caída para a esquerda |
| `Main.java`, `ConsoleUtils.java` | Menu interativo |
| `Testes.java` | Testes e comparação |
