# Othello_AI_minimax

**NOTE BEFORE START:**
1. In the folder, there is a ‘KautzPlayer’ folder. This is the default test class for our course. It just looks one head ahead - so it’s the dummy test class. 

LittleGreenMan is my group (of 2) name. So I would recommend using this AI/program. 

2. The only thing that my group coded is everything in LittleGreenMan folder. The rest (the boards, and GM (e.g. java panel) ) were given since it was an AI course, rather than a game-development course.

**Just for a quick test-run:**
(If you want to, you can make sure LittleGreenMan compiles.)

1. Go to terminal (or cmd) and navigate to OthelloProject Folder.

2. ENTER: ./ tournament gui LittleGreenMan LittleGreenMan 10 500 0

**How to run:**
1. ENTER: ./ tournament MODE BLACK_PIECE WHITE_PIECE DEPTH TIME_LIMIT_1 TIME_LIMIT2

**MODE: can be either:**
(a) text 	; which will print the moves on the terminal
(b) gui 	; which will use a java panel to show (this will have to be used if you are planning with the ai/program)

**BLACK_PIECE and/or WHITE_PIECE: can be either:**

(a) -human	; which means you are going to input the moves (Also, MODE will have to be ‘gui’ for this to work.

(b) <Directory of AI> ; in this case, you can use LittleGreenMan.

**DEPTH:** means how many steps ahead do you want to look. 
This can be any int.

**TIME_LIMIT_1:** means how long (in milliseconds) each step can take.
* Note: TIME_LIMIT_1 > 0 will override DEPTH. It will go as deep (depth) as it can within the time limit.
* Note: if TIME_LIMIT_1 == 0, DEPTH will be used to determine how long it takes. TIME_LIMIT_1 is hard-coded to a huge number. 

**TIME_LIMIT_2:** is supposed to be how long the whole game should take. But the TA said to ignore this, so enter ‘0’.

* DEPTH, TIME_LIMIT_1, TIME_LIMIT_2 only limits the AI/program’s decision. Human players can take forever to make a move.

**Last Note:**
So if you want to run against the AI, you can do this by ENTERING:
./ tournament gui -human LittleGreenMan 5 0 0 	[to play as black piece]
OR
./ tournament gui LittleGreenMan -human 5 0 0 	[to play as white piece]

You can also play LittleGreenMan against itself, or against KautzPlayer!

