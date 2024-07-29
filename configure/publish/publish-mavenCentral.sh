GPG_TTY=$(tty)
export GPG_TTY

./mvnw clean install package deploy -DskipTests -Dspotbugs.skip
