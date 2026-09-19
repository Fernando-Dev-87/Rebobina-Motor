package com.example.data.datasource

import com.example.domain.model.FileCategory
import com.example.domain.model.RebobinadorFile

object RebobinadorFilesRepository {

    const val FACEBOOK_FILES_URL = "https://www.facebook.com/groups/395592083967391/files/files"
    const val OREBOBINADOR_WEBSITE_URL = "https://www.orebobinador.com.br"

    val files: List<RebobinadorFile> = listOf(
        RebobinadorFile(
            id = "apostila_018_09",
            title = "Apostila 018/09 - Cálculo de Motores sem Dados (Pela Ferragem)",
            fileName = "Apostila_018_09_Calculo_Ferragem_Iltonn.pdf",
            category = FileCategory.APOSTILAS,
            author = "Iltonn / O Rebobinador",
            dateAdded = "Atualizado na Comunidade",
            fileSize = "3.8 MB",
            fileExtension = "PDF",
            description = "A clássica apostila de bancada para motores que chegam sem placa de identificação ou dados. Ensina a calcular fluxo magnético, espiras por fase, área da ranhura e bitola AWG através do paquímetro.",
            tags = listOf("Ferragem", "Sem Placa", "Cálculo", "Fórmula", "Estator", "Iltonn"),
            technicalContent = """
=== APOSTILA 018/09: CÁLCULO PELAS MEDIDAS DA FERRAGEM ===
Autor: Iltonn / Comunidade O Rebobinador
Uso: Dimensionamento de estator sem placa de identificação.

1. ROTAÇÃO SÍNCRONA:
   ns = (120 * f) / 2p
   Ex: 4 polos a 60Hz = 1800 RPM (nominal ~1740 RPM).

2. PASSO POLAR (τ):
   τ = (π * D) / 2p [mm]
   Onde D é o diâmetro interno do estator em mm e 2p é o número de polos.
   Passo polar em ranhuras: Yp = S / 2p (S = total de ranhuras).
   Passo da bobina: 1 a (1 + Yp).

3. ÁREA POLAR (Ap) EM cm²:
   Ap = (τ / 10) * (L / 10) [cm²]
   Onde L é o comprimento do pacote de chapas em mm.

4. FLUXO MAGNÉTICO POR POLO (Φ):
   Indução de entreferro (B) recomendada: 6.500 a 7.500 Gauss (0,65 a 0,75 T).
   Φ (Maxwell) = B * Ap * (2 / π)

5. FORÇA CONTRA-ELETROMOTRIZ (Ef):
   Ef = 0,95 * Vfase
   Em Triângulo (Δ) 220V: Ef = 0,95 * 220 = 209 V.
   Em Estrela (Y) 380V: Ef = 0,95 * (380 / √3) = 208,5 V.

6. ESPIRAS TOTAIS EM SÉRIE POR FASE (Nf):
   Nf = (Ef * 10^8) / (4,44 * f * Φ_Maxwell * kw)
   Onde f = 60Hz e kw = 0,95 a 0,96 (fator de enrolamento).

7. ESPIRAS POR BOBINA (Nb):
   - Camada Dupla: Bf = S / 3 bobinas por fase.
     Nb = Nf / Bf.
   - Camada Simples: Bf = S / 6 bobinas por fase.
     Nb = Nf / Bf.

8. CONDUTORES POR RANHURA (Zr):
   Camada Dupla: Zr = 2 * Nb condutores.
   Camada Simples: Zr = Nb condutores.

9. SEÇÃO DO CONDUTOR E TABELA AWG:
   Área da Ranhura (Ar): Ar = ((b1 + b2) / 2) * h [mm²]
   Área útil de cobre (Acobre): Ar * ke (ke = 0,35 a 0,40 fator de enchimento).
   Seção máxima por condutor: Scobre = Acobre / Zr [mm²].
   Consulte a tabela AWG para selecionar o fio correspondente.
            """.trimIndent()
        ),
        RebobinadorFile(
            id = "esquema_12_pontas",
            title = "Caderno de Esquemas Trifásicos de 12 Pontas (220/380/440/760V)",
            fileName = "Esquemas_Trifasicos_12_Pontas_O_Rebobinador.pdf",
            category = FileCategory.ESQUEMAS_TRIFASICOS,
            author = "Manoweber / O Rebobinador",
            dateAdded = "Grupo Arquivos Facebook",
            fileSize = "2.4 MB",
            fileExtension = "PDF",
            description = "Diagrama completo com os 4 níveis de tensão para motores trifásicos de 12 pontas: Duplo Triângulo (220V), Duplo Estrela (380V), Triângulo Série (440V) e Estrela Série (760V).",
            tags = listOf("12 Pontas", "Trifásico", "Esquema", "220V", "380V", "440V", "760V"),
            technicalContent = """
=== ESQUEMA DE LIGAÇÃO DE 12 PONTAS (TRIFÁSICO) ===
Origem: Arquivos O Rebobinador

IDENTIFICAÇÃO DAS BOBINAS:
Fase 1: Cabos 1 - 4 e 7 - 10
Fase 2: Cabos 2 - 5 e 8 - 11
Fase 3: Cabos 3 - 6 e 9 - 12

1. LIGAÇÃO 220V (DUPLO TRIÂNGULO - ΔΔ):
   L1 (Rede): Juntar 1, 6, 7 e 12
   L2 (Rede): Juntar 2, 4, 8 e 10
   L3 (Rede): Juntar 3, 5, 9 e 11

2. LIGAÇÃO 380V (DUPLO ESTRELA - YY):
   L1 (Rede): Juntar 1 e 7
   L2 (Rede): Juntar 2 e 8
   L3 (Rede): Juntar 3 e 9
   Ponto Neutro 1: Fechar juntos 4, 5 e 6
   Ponto Neutro 2: Fechar juntos 10, 11 e 12

3. LIGAÇÃO 440V (TRIÂNGULO SÉRIE - Δ):
   L1 (Rede): Juntar 1 e 12
   L2 (Rede): Juntar 2 e 10
   L3 (Rede): Juntar 3 e 11
   Emenda 1: Juntar 4 com 7
   Emenda 2: Juntar 5 com 8
   Emenda 3: Juntar 6 com 9

4. LIGAÇÃO 760V (ESTRELA SÉRIE - Y):
   L1 (Rede): Ligar no 1
   L2 (Rede): Ligar no 2
   L3 (Rede): Ligar no 3
   Emenda 1: Juntar 4 com 7
   Emenda 2: Juntar 5 com 8
   Emenda 3: Juntar 6 com 9
   Ponto Estrela: Fechar juntos 10, 11 e 12
            """.trimIndent()
        ),
        RebobinadorFile(
            id = "esquema_6_pontas",
            title = "Esquemas Trifásicos de 6 Pontas (Triângulo 220V e Estrela 380V)",
            fileName = "Esquema_Trifasico_6_Pontas_Serie_Paralelo.pdf",
            category = FileCategory.ESQUEMAS_TRIFASICOS,
            author = "O Rebobinador",
            dateAdded = "Grupo Arquivos Facebook",
            fileSize = "1.5 MB",
            fileExtension = "PDF",
            description = "O esquema mais utilizado em oficinas brasileiras. Demonstração de ligação em Triângulo (220V) e Estrela (380V), com diagramas de cabeças de bobina e saída de cabos.",
            tags = listOf("6 Pontas", "Trifásico", "220V", "380V", "Delta", "Estrela"),
            technicalContent = """
=== ESQUEMA DE LIGAÇÃO DE 6 PONTAS (TRIFÁSICO) ===
Origem: Arquivos O Rebobinador

IDENTIFICAÇÃO DAS 3 FASES:
Fase R (U): Terminais 1 e 4
Fase S (V): Terminais 2 e 5
Fase T (W): Terminais 3 e 6

1. LIGAÇÃO TRIÂNGULO (Δ) - 220V (Menor Tensão):
   Rede L1: Juntar cabos 1 e 6
   Rede L2: Juntar cabos 2 e 4
   Rede L3: Juntar cabos 3 e 5
   * Corrente nominal de linha é maior: In_Δ = √3 * Ifase

2. LIGAÇÃO ESTRELA (Y) - 380V (Maior Tensão):
   Rede L1: Ligar no cabo 1
   Rede L2: Ligar no cabo 2
   Rede L3: Ligar no cabo 3
   Ponto Estrela (Neutro isolado): Fechar juntos cabos 4, 5 e 6
   * Corrente nominal de linha: In_Y = In_Δ / √3
   * Ideal para partida estrela-triângulo com contatoras.
            """.trimIndent()
        ),
        RebobinadorFile(
            id = "esquema_mono_capacitor",
            title = "Esquemas de Motores Monofásicos com Capacitor (4 e 6 Pontas)",
            fileName = "Esquemas_Monofasicos_Capacitor_Partida_Permanente.pdf",
            category = FileCategory.ESQUEMAS_MONOFASICOS,
            author = "O Rebobinador / Bancada",
            dateAdded = "Grupo Arquivos Facebook",
            fileSize = "2.1 MB",
            fileExtension = "PDF",
            description = "Esquemas de motores monofásicos de 110V/220V com bobina principal (trabalho) dividida em 2 seções e bobina auxiliar com capacitor eletrolítico e centrífugo/platinado.",
            tags = listOf("Monofásico", "Capacitor", "Platinado", "110V", "220V", "Sentido Giro"),
            technicalContent = """
=== MOTOR MONOFÁSICO DE 6 CABOS (110V / 220V) ===
Origem: Arquivos O Rebobinador

CONSTITUIÇÃO:
- Enrolamento Principal (Trabalho):
  Seção 1: Cabos 1 e 2
  Seção 2: Cabos 3 e 4
- Enrolamento Auxiliar (Partida):
  Cabos 5 e 6 (em série com capacitor e platinado/centrífugo)

1. LIGAÇÃO 110V / 127V (PARALELO):
   L1 (Fase): Juntar cabos 1, 3 e 5
   L2 (Neutro): Juntar cabos 2, 4 e 6

2. LIGAÇÃO 220V (SÉRIE):
   L1 (Fase): Juntar cabos 1 e 5
   L2 (Fase/Neutro): Ligar no cabo 4
   Isolamento: Juntar cabos 2, 3 e 6 e isolar na fita.

3. INVERSÃO DO SENTIDO DE ROTAÇÃO:
   Para inverter o sentido de giro, basta inverter os cabos 5 e 6 entre si:
   Trocar cabo 5 por 6 na ligação da rede.
            """.trimIndent()
        ),
        RebobinadorFile(
            id = "tabela_awg_esmaltado",
            title = "Tabela Prática de Fios de Cobre Esmaltados AWG (Oficina)",
            fileName = "Tabela_AWG_Cobre_Esmaltado_Oficina.pdf",
            category = FileCategory.TABELAS_TECNICAS,
            author = "O Rebobinador",
            dateAdded = "Grupo Arquivos Facebook",
            fileSize = "1.1 MB",
            fileExtension = "PDF",
            description = "Tabela de referência para enrolamento de motores contendo seção em mm², diâmetro do cobre nu, diâmetro esmaltado com verniz classe H (180°C), peso (kg/km) e equivalência de fios.",
            tags = listOf("Tabela AWG", "Fio Esmaltado", "Cobre", "Seção mm²", "Diâmetro"),
            technicalContent = """
=== TABELA AWG PARA REBOBINAMENTO (CLASSE H - 180°C) ===
Origem: Comunidade O Rebobinador

AWG | Seção mm² | Ø Nu mm | Ø c/ Verniz mm | Resistência Ω/km (20°C)
------------------------------------------------------------------
10  | 5,260     | 2,590   | 2,690          | 3,28
11  | 4,170     | 2,300   | 2,400          | 4,13
12  | 3,310     | 2,050   | 2,140          | 5,21
13  | 2,620     | 1,830   | 1,910          | 6,57
14  | 2,080     | 1,630   | 1,710          | 8,29
15  | 1,650     | 1,450   | 1,520          | 10,45
16  | 1,310     | 1,290   | 1,360          | 13,17
17  | 1,040     | 1,150   | 1,220          | 16,61
18  | 0,823     | 1,024   | 1,090          | 20,95
19  | 0,653     | 0,912   | 0,973          | 26,42
20  | 0,518     | 0,812   | 0,871          | 33,31
21  | 0,410     | 0,723   | 0,779          | 42,00
22  | 0,326     | 0,644   | 0,698          | 52,96
23  | 0,258     | 0,573   | 0,625          | 66,79
24  | 0,205     | 0,511   | 0,560          | 84,21
25  | 0,162     | 0,455   | 0,501          | 106,2
26  | 0,129     | 0,405   | 0,449          | 133,9
27  | 0,102     | 0,361   | 0,402          | 168,9
28  | 0,081     | 0,321   | 0,359          | 213,0
29  | 0,064     | 0,286   | 0,322          | 268,5
30  | 0,051     | 0,255   | 0,289          | 338,6

REGRA PRÁTICA DE SUBSTITUIÇÃO (Fios em Paralelo):
- Somar 3 números no AWG divide a seção por 2.
- 1 fio AWG 18 equivale a 2 fios AWG 21 em paralelo.
- 1 fio AWG 16 equivale a 2 fios AWG 19 em paralelo.
            """.trimIndent()
        ),
        RebobinadorFile(
            id = "tabela_weg_w21_w22",
            title = "Tabela de Bobinagem de Motores WEG W21 e W22 (Carcaças 63 a 160)",
            fileName = "Tabela_Bobinagem_WEG_W21_W22_Padrao.pdf",
            category = FileCategory.TABELAS_TECNICAS,
            author = "O Rebobinador / WEG",
            dateAdded = "Grupo Arquivos Facebook",
            fileSize = "4.2 MB",
            fileExtension = "PDF",
            description = "Caderno de dados originais de fábrica WEG: potência em CV, carcaça, número de ranhuras, passo de bobina, quantidade de espiras e bitola dos condutores para motores de 2, 4 e 6 polos.",
            tags = listOf("WEG", "W21", "W22", "Dados Originais", "Carcaça", "Espiras"),
            technicalContent = """
=== DADOS DE BOBINAGEM PADRÃO WEG W21 / W22 (4 POLOS - 60Hz) ===
Origem: Arquivos O Rebobinador

1. Potência: 1,0 CV (Carcaça 71)
   - Ranhuras: 24 | Camada: Dupla
   - Passo de Bobina: 1 a 6
   - Espiras por Bobina: 48 espiras
   - Fio: 1x AWG 20 (ou 2x AWG 23)
   - Tensão: 220/380V (6 cabos)

2. Potência: 2,0 CV (Carcaça 80)
   - Ranhuras: 36 | Camada: Dupla
   - Passo de Bobina: 1 a 8, 1 a 10
   - Espiras por Bobina: 32 espiras
   - Fio: 1x AWG 18 (ou 2x AWG 21)
   - Tensão: 220/380V

3. Potência: 3,0 CV (Carcaça 90S)
   - Ranhuras: 36 | Camada: Dupla
   - Passo de Bobina: 1 a 8, 1 a 10
   - Espiras por Bobina: 24 espiras
   - Fio: 1x AWG 16 (ou 2x AWG 19)
   - Tensão: 220/380V

4. Potência: 5,0 CV (Carcaça 100L)
   - Ranhuras: 36 | Camada: Dupla
   - Passo de Bobina: 1 a 8, 1 a 10
   - Espiras por Bobina: 17 espiras
   - Fio: 1x AWG 14 (ou 2x AWG 17)
   - Tensão: 220/380/440V (12 cabos)

5. Potência: 7,5 CV (Carcaça 112M)
   - Ranhuras: 36 | Camada: Dupla
   - Passo de Bobina: 1 a 8, 1 a 10
   - Espiras por Bobina: 13 espiras
   - Fio: 2x AWG 16 em paralelo
   - Tensão: 220/380/440/760V (12 cabos)
            """.trimIndent()
        ),
        RebobinadorFile(
            id = "esquema_dahlander",
            title = "Esquema de Motores Dahlander de Dupla Velocidade (Δ/YY)",
            fileName = "Esquema_Dahlander_Dupla_Velocidade_Torque_Constante.pdf",
            category = FileCategory.MOTORES_ESPECIAIS,
            author = "O Rebobinador",
            dateAdded = "Grupo Arquivos Facebook",
            fileSize = "1.8 MB",
            fileExtension = "PDF",
            description = "Diagrama de bobinagem para motor Dahlander com derivação central nas bobinas para alternar número de polos (ex.: 4 polos / 2 polos - 1800/3600 RPM) mantendo a mesma frequência de rede.",
            tags = listOf("Dahlander", "Dupla Velocidade", "Delta/YY", "4P/2P", "Especial"),
            technicalContent = """
=== MOTOR DAHLANDER DE DUPLA VELOCIDADE (Δ / YY) ===
Origem: Arquivos O Rebobinador

PRINCÍPIO:
O enrolamento possui derivação central em cada uma das fases, permitindo alterar o sentido da corrente em metade das bobinas, dobrando ou cortando pela metade a quantidade de polos magnéticos (ex: 4 polos / 2 polos).

CONEXÕES (6 CABOS: 1U, 1V, 1W e 2U, 2V, 2W):

1. BAIXA VELOCIDADE (Ex: 4 Polos - 1800 RPM):
   - Conexão em Triângulo (Δ)
   - Rede L1, L2, L3 nos terminais 1U, 1V, 1W.
   - Terminais 2U, 2V, 2W ficam abertos e isolados.

2. ALTA VELOCIDADE (Ex: 2 Polos - 3600 RPM):
   - Conexão em Duplo Estrela (YY)
   - Curto-circuitar os terminais 1U, 1V, 1W juntos (ponto estrela).
   - Aplicar a rede L1, L2, L3 nos terminais 2U, 2V, 2W.
            """.trimIndent()
        ),
        RebobinadorFile(
            id = "passos_concentrico_imbricado",
            title = "Diagrama de Passos de Bobinas: Concêntrico vs Imbricado",
            fileName = "Passos_Bobinas_Concentrico_Imbricado.pdf",
            category = FileCategory.APOSTILAS,
            author = "O Rebobinador",
            dateAdded = "Grupo Arquivos Facebook",
            fileSize = "2.9 MB",
            fileExtension = "PDF",
            description = "Manual explicativo de formas geométricas de bobinas no estator: bobinagem concêntrica por grupos ou imbricada de passo constante, cálculo do passo encurtado para atenuação de harmônicos.",
            tags = listOf("Concêntrico", "Imbricado", "Passo Encurtado", "Harmônicos", "Cabeça de Bobina"),
            technicalContent = """
=== ENROLAMENTO CONCÊNTRICO VS IMBRICADO ===
Origem: Arquivos O Rebobinador

1. ENROLAMENTO CONCÊNTRICO:
   - Bobinas de tamanhos diferentes, encaixadas uma dentro da outra.
   - Exemplo em estator de 24 ranhuras (4 polos): Passos 1 a 4, 1 a 6.
   - Vantagens: Menor comprimento médio de condutor, facilidade de inserção manual em motores monofásicos de bombas e compressores.

2. ENROLAMENTO IMBRICADO (CAMADA DUPLA):
   - Todas as bobinas são idênticas em molde e perímetro.
   - Passo de bobina constante (ex.: 1 a 10 em 36 ranhuras 4 polos).
   - Cada ranhura recebe o lado inferior de uma bobina e o lado superior de outra.
   - Vantagens: Equilíbrio magnético perfeito, simetria nas 3 fases, menor indutância de dispersão.

3. ENCURTAMENTO DE PASSO (kp):
   - Reduz o passo polar de 1 ranhura para atenuar harmônicos de 5ª e 7ª ordem.
   - Ex: Se o passo pleno for 1 a 10 (9 ranhuras), o encurtado será 1 a 9 (8 ranhuras).
   - Fator de encurtamento: kp = sen( (β / 2) * (180° / Yp) ).
            """.trimIndent()
        )
    )
}
