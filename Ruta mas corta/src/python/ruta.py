import pandas as pd
import networkx as nx
import matplotlib.pyplot as plt

# Leer el archivo CSV
df = pd.read_csv('rutas.csv', header=None, names=['node1', 'node2', 'weight'])

# Crear un grafo dirigido
G = nx.DiGraph()

# Añadir nodos y aristas con pesos (distancias) desde el archivo CSV
for _, row in df.iterrows():
    G.add_edge(row['node1'], row['node2'], weight=row['weight'])

# Seleccionar el nodo fuente
source = 1  # Puedes cambiar el nodo fuente según necesites

# Calcular las distancias utilizando Dijkstra
distances = nx.single_source_dijkstra_path_length(G, source)

# Dibujar el grafo
pos = nx.spring_layout(G)
nx.draw(G, pos, with_labels=True, node_color='lightblue', node_size=700, font_size=10)
labels = nx.get_edge_attributes(G, 'weight')
nx.draw_networkx_edge_labels(G, pos, edge_labels=labels)

# Crear una lista de distancias como texto
distance_text = '\n'.join([f'{source} ⟶ {node}: {dist}' for node, dist in distances.items()])

# Añadir el texto de distancias al gráfico
plt.gcf().text(0.02, 0.02, distance_text, fontsize=10, bbox=dict(facecolor='white', alpha=0.5))

plt.title(f'Distancias desde el nodo {source}')
plt.show()

# Imprimir las distancias
print(f'Distancias desde el nodo {source}:')
for node, dist in distances.items():
    print(f'{source} -> {node}: {dist}')
