# Makefile

TARGETS=Growth.pdf Compartmental.pdf

FORCE:
	make $(TARGETS)


%.pdf: %.md
	pandoc -t beamer $< -o $@

clean:
	rm -f *~ *.png $(TARGETS)


# eof
