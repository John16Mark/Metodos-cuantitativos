import pandas as pd
import networkx as nx
import matplotlib.pyplot as plt
import sys
import heapq
 
argv = len(sys.argv)
if(argv != 2):
    print("\033[31mNúmero de argumentos inválido")
    print("\033[0mpython ruta.py n_nodos")
    exit(0)

##########################################
#               Definiciones
##########################################
class Grafo:
    def __init__(self, V: int):
        self.V = V
        self.adj = [[] for _ in range(V)]
    def nuevaArista(self, u: int, v: int, w: int):
        self.adj[u].append((v, w))
    def rutaMasCorta(self, src: int):
        heap = []
        heapq.heappush(heap, (0, src))
        dist = [float('inf')] * self.V
        dist[src] = 0
        while heap:
            d, u = heapq.heappop(heap)
            for v, distancia in self.adj[u]:
                if dist[v] > dist[u] + distancia:
                    dist[v] = dist[u] + distancia
                    heapq.heappush(heap, (dist[v], v))
        
        texto = ["", ""]
        texto[0] = "\033[92mDistancias del nodo origen a todos los nodos:\033[0m\n"
        texto[1] = "Origen -> destino\n"
        for i in range(self.V):
            texto[0] += f"Nodo {src+1} \t-> Nodo {i+1} \t {dist[i]}\n"
            if i < self.V - 1:
                texto[1] += f"{src+1} -> {i+1} : {dist[i]}\n"
            else:
                texto[1] += f"{src+1} -> {i+1} : {dist[i]}"

        return texto

V = int(sys.argv[1])
grafo = Grafo(V)
G = nx.DiGraph()

##########################################
#   Lectura de archivos y alm de datos
##########################################
df = pd.read_csv('src/python/rutas.csv', header=None, names=['nodo1', 'nodo2', 'distancia'])
for _, row in df.iterrows():
    grafo.nuevaArista(int(row['nodo1'])-1, int(row['nodo2'])-1, row['distancia'])
    G.add_edge(row['nodo1'], row['nodo2'], weight=row['distancia'])

nodos_arch = pd.read_csv('src/python/origen.csv', header=None, names=['tipo', 'nodo'])
origen = nodos_arch.loc[nodos_arch['tipo'] == 'origen', 'nodo'].values[0]
destino = nodos_arch.loc[nodos_arch['tipo'] == 'destino', 'nodo'].values[0]

##########################################
#             Ruta más corta
##########################################
resultado = grafo.rutaMasCorta(origen-1)
print(resultado[0])

##########################################
#           Gráficos Matplotlib
##########################################

# Calcular las distancias utilizando Dijkstra
distances = nx.single_source_dijkstra_path_length(G, origen)

# Dibujar el grafo
pos = nx.spring_layout(G)
nx.draw(G, pos, with_labels=True, node_color='#68E0FF', node_size=700, font_size=10)
labels = nx.get_edge_attributes(G, 'weight')
nx.draw_networkx_edge_labels(G, pos, edge_labels=labels)

# Añadir el texto de distancias al gráfico
plt.gcf().text(0.02, 0.02, resultado[1], fontsize=10, bbox=dict(facecolor='#D4C9FF', alpha=0.5))

# Dijsktra para terminar de dibujar y colorear el grafo
ruta = nx.dijkstra_path(G, origen, destino)
distancia = nx.dijkstra_path_length(G, origen, destino)
path_edges = list(zip(ruta, ruta[1:]))
nx.draw_networkx_nodes(G, pos, nodelist=ruta, node_color='#FF561E', node_size=700)
nx.draw_networkx_edges(G, pos, edgelist=path_edges, edge_color='#FF561E')

plt.title(f'Distancias desde el nodo {origen}')
plt.show()
