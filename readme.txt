"Cellular atomata" creative coding project that I came up with and have spent most of my HS senior year working on.

Basically, the cells that are 'lit up' are all chosen by huge games of minesweeper that the computer plays in the background.
Usually multiple games are being played at once (I usually run 3 minesweeper-solver threads) which causes overlap and interference in the output.

color key:
	- blue:  A flag was placed
	- gray:  intensity of gray indicates the number of mines around the revealed cell
	- black: either has no mines around it, or has not been revealed yet

I had an old version that I wrote with the processing IDE, but it felt really hacky. This new version is greatly improved in terms of speed, reliability, multithreaded correctness, and code quality.

- Aaron Golliver, 2011/2012


Processing is Open Source Software. The PDE (Processing Development Environment) is released under the GNU GPL (General Public License). The export libraries (also known as 'core') are released under the GNU LGPL (Lesser General Public License). There's more information about Processing and Open Source in the FAQ and more information about the GNU GPL and GNU LGPL at opensource.org. Please contribute to Processing!

THE Processing SOFTWARE IS PROVIDED TO YOU "AS IS," AND WE MAKE NO EXPRESS OR IMPLIED WARRANTIES WHATSOEVER WITH RESPECT TO ITS FUNCTIONALITY, OPERABILITY, OR USE, INCLUDING, WITHOUT LIMITATION, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR INFRINGEMENT. WE EXPRESSLY DISCLAIM ANY LIABILITY WHATSOEVER FOR ANY DIRECT, INDIRECT, CONSEQUENTIAL, INCIDENTAL OR SPECIAL DAMAGES, INCLUDING, WITHOUT LIMITATION, LOST REVENUES, LOST PROFITS, LOSSES RESULTING FROM BUSINESS INTERRUPTION OR LOSS OF DATA, REGARDLESS OF THE FORM OF ACTION OR LEGAL THEORY UNDER WHICH THE LIABILITY MAY BE ASSERTED, EVEN IF ADVISED OF THE POSSIBILITY OR LIKELIHOOD OF SUCH DAMAGES.

THE MineFlow SOFTWARE IS PROVIDED TO YOU "AS IS," AND I MAKE NO EXPRESS OR IMPLIED WARRANTIES WHATSOEVER WITH RESPECT TO ITS FUNCTIONALITY, OPERABILITY, OR USE, INCLUDING, WITHOUT LIMITATION, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR INFRINGEMENT. I EXPRESSLY DISCLAIM ANY LIABILITY WHATSOEVER FOR ANY DIRECT, INDIRECT, CONSEQUENTIAL, INCIDENTAL OR SPECIAL DAMAGES, INCLUDING, WITHOUT LIMITATION, LOST REVENUES, LOST PROFITS, LOSSES RESULTING FROM BUSINESS INTERRUPTION OR LOSS OF DATA, REGARDLESS OF THE FORM OF ACTION OR LEGAL THEORY UNDER WHICH THE LIABILITY MAY BE ASSERTED, EVEN IF ADVISED OF THE POSSIBILITY OR LIKELIHOOD OF SUCH DAMAGES.



