@echo off
title Server Initialize
java -Xmx1024m -Xmx1024m -cp bin;libs/mina.jar;libs/netty.jar;libs/slf4j.jar;libs/log4j-1.2.15.jar;libs/slf4j-nop.jar; server.rs2.Loader
pause