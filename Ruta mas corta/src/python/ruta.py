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
    def addEdge(self, u: int, v: int, w: int):
        self.adj[u].append((v, w))
    def shortestPath(self, src: int):
        heap = []
        heapq.heappush(heap, (0, src))
        dist = [float('inf')] * self.V
        dist[src] = 0
        while heap:
            d, u = heapq.heappop(heap)
            for v, weight in self.adj[u]:
                if dist[v] > dist[u] + weight:
                    dist[v] = dist[u] + weight
                    heapq.heappush(heap, (dist[v], v))
        
        result = ["", ""]
        result[0] = "\033[92mDistancias del nodo origen a todos los nodos:\033[0m\n"
        result[1] = "Origen -> destino\n"
        for i in range(self.V):
            result[0] += f"Nodo {src+1} \t-> Nodo {i+1} \t {dist[i]}\n"
            if i < self.V - 1:
                result[1] += f"{src+1} -> {i+1} : {dist[i]}\n"
            else:
                result[1] += f"{src+1} -> {i+1} : {dist[i]}"

        return result

V = int(sys.argv[1])
grafo = Grafo(V)
G = nx.DiGraph()

##########################################
#   Lectura de archivos y alm de datos
##########################################
df = pd.read_csv('src/python/rutas.csv', header=None, names=['node1', 'node2', 'weight'])
for _, row in df.iterrows():
    grafo.addEdge(int(row['node1'])-1, int(row['node2'])-1, row['weight'])
    G.add_edge(row['node1'], row['node2'], weight=row['weight'])

nodes_df = pd.read_csv('src/python/origen.csv', header=None, names=['type', 'node'])
origen = nodes_df.loc[nodes_df['type'] == 'origen', 'node'].values[0]
destino = nodes_df.loc[nodes_df['type'] == 'destino', 'node'].values[0]

##########################################
#             Ruta más corta
##########################################
resultado = grafo.shortestPath(origen-1)
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

# Dijsktra para terminar de dibujar el grafo
ruta = nx.dijkstra_path(G, origen, destino)
distancia = nx.dijkstra_path_length(G, origen, destino)
path_edges = list(zip(ruta, ruta[1:]))
nx.draw_networkx_nodes(G, pos, nodelist=ruta, node_color='#FF561E', node_size=700)
nx.draw_networkx_edges(G, pos, edgelist=path_edges, edge_color='#FF561E')

plt.title(f'Distancias desde el nodo {origen}')
plt.show()
