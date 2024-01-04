import pandas as pd
import matplotlib.pyplot as plt
import csv

molecules = pd.read_csv("src/main/resources/data.csv")

def draw_step(stepId):
    molecules_by_step = molecules[molecules['STEP'] == stepId]
    fig = plt.figure()
    ax = fig.add_subplot(projection='3d')

    colors = ["red", "green", "blue"]
    ax.scatter(molecules_by_step['X'], molecules_by_step['Y'], molecules_by_step['Z'], c=colors)

    ax.set_xlabel('X')
    ax.set_ylabel('Y')
    ax.set_zlabel('Z')

    plt.show()

def draw_trajectories():
    colors = ["red", "green", "blue"]
    fig = plt.figure()
    ax = fig.add_subplot(111, projection = '3d')
    for i in range(0, 3):
        points = molecules[molecules['ID'] == i]
        ax.plot(points['X'], points['Y'], points['Z'], marker = i, c=colors[i])
    ax.set_xlabel('X')
    ax.set_ylabel('Y')
    ax.set_zlabel('Z')
    plt.show()

draw_trajectories()