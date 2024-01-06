import pandas as pd
import matplotlib.pyplot as plt
import csv

molecules = pd.read_csv("src/main/resources/data.csv")

def draw_trajectories():
    colors = ["red", "green", "blue"]
    fig = plt.figure()
    ax = fig.add_subplot(111, projection = '3d')
    for i in range(0, 3):
        points = molecules[molecules['ID'] == i]
        ax.scatter(points['X'], points['Y'], points['Z'], marker = i, c=colors[i])
    ax.set_xlabel('X')
    ax.set_ylabel('Y')
    ax.set_zlabel('Z')
    plt.show()

draw_trajectories()