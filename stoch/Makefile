# Makefile


FORCE:
	sbt mdoc
	cd target/mdoc ; make

edit:
	emacs *.md Makefile build.sbt src/main/scala/*.scala docs/*.md &

clean:
	rm -f *~
	cd target/mdoc ; make clean


# eof
