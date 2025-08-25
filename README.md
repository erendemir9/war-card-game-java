# 🃏 War Card Game (Java Swing)

This is a **Java Swing GUI** implementation of the classic **War card game**, built with **Object-Oriented Programming (OOP)** principles.  
The project includes a graphical interface, user accounts, saving/loading progress, and different game modes.

---

## 🎮 Features

### 🃏 Core Gameplay

- `deck` package → Card creation, catalog, and shuffling logic  
- `file` package → Reading and saving users/games  
- `game` package → Core game engine, game states, and game list management  
- `gui` package → Menus, game screens, sound system  
- `player` package → Human, guest, computer players, and user list management  
- `main` package → Main entry point for starting the application  

### 🖥️ Graphical User Interface

- Built with **Java Swing**
- Interactive menus, buttons, panels, and labels
- Custom background images and card visuals

### 👤 User Accounts

- Create and log in with a username
- Save game progress in a `.txt` file
- Continue playing from where you left off
- Guest mode available (no account needed)

### 🎲 Game Modes

- **Single Player** → Play against computer AI
- **Two Players** → Local multiplayer with a friend
- **Guest Mode** → Quick play without account saving

### 🚀 Future Plans

- Replace `.txt` file saving with **SQL database** (MySQL/PostgreSQL)
- Add **online multiplayer** functionality
- Improve card animations and GUI effects

---

## 🛠️ Technologies

- **Java (JDK 17+)**
- **Java Swing** for GUI
- **File I/O** for saving accounts and progress
- Planned: **SQL Database integration**

---

## 📜 License

This project is licensed under the **MIT License**.  
See the [LICENSE](./LICENSE) file for details.
