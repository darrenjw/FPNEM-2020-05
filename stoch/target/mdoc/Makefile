# Makefile

TARGETS=Stochastic.pdf

FORCE:
	make $(TARGETS)


%.pdf: %.md
	pandoc -t beamer $< -o $@

clean:
	rm -f *~ *.png $(TARGETS)


# eof
