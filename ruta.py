import networkx as nx
import matplotlib.pyplot as plt

# Crear un grafo
G = nx.Graph()

# Añadir nodos y aristas
edges = [
    ('A', 'B', 1),
    ('A', 'C', 4),
    ('B', 'C', 2),
    ('B', 'D', 5),
    ('C', 'D', 1)
]
G.add_weighted_edges_from(edges)

# Calcular la ruta más corta utilizando Dijkstra
source = 'A'
target = 'D'
path = nx.dijkstra_path(G, source, target)
length = nx.dijkstra_path_length(G, source, target)

# Dibujar el grafo
pos = nx.spring_layout(G)
nx.draw(G, pos, with_labels=True, node_color='lightblue', node_size=700, font_size=10)
labels = nx.get_edge_attributes(G, 'weight')
nx.draw_networkx_edge_labels(G, pos, edge_labels=labels)

# Resaltar la ruta más corta
path_edges = list(zip(path, path[1:]))
nx.draw_networkx_nodes(G, pos, nodelist=path, node_color='red')
nx.draw_networkx_edges(G, pos, edgelist=path_edges, edge_color='red', width=2)

plt.title(f'Ruta más corta de {source} a {target}: {path} (Longitud: {length})')
plt.show()