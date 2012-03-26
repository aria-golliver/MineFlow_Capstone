"Cellular atomata" type creative coding project that I came up with and have spent most of my HS senior year working on.

Basically, the cells that are 'lit up' are all chosen by a huge game of minesweeper that the computer is playing in the background.
Usually multiple games are being played at once (I usually run 3 minesweeper-solver threads) which causes overlap and interference in the output.

I had an old version that I wrote with the processing IDE, but it felt really hacky. Now I'm making a really hacky new version, but I feel like it's much easier to follow.
I use hashmaps to hold all the minesweeper cells, but I do it a little to 'clojure' style for my liking. If I could rewrite it completely, it'd do it in 100% clojure, but I don't have that kind of skill just quite yet.

- Aaron Golliver

also, comments for the code will happen eventually, I promise.