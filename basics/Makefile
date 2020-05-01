# Makefile

TARGETS=Growth.pdf

FORCE:
	sbt mdoc
	make $(TARGETS)

view: $(TARGETS)
	xdg-open Growth.pdf

%.pdf: target/mdoc/%.md
	pandoc -t beamer $< -o $@


edit:
	emacs *.md Makefile build.sbt src/main/scala/*.scala docs/*.md &
