CLASS_PATH="target/test-classes:target/dependency/*"
MAIN_CLASS="com.taobao.test.modules.tfs.perf.data.ModTfsDataEidolon"

JAVA_OPTS="-Xms1536m -Xmx1536m -XX:NewSize=320m -XX:MaxNewSize=320m -XX:PermSize=96m -XX:MaxPermSize=256m"
JAVA_OPTS="$JAVA_OPTS -XX:MaxTenuringThreshold=5 -Djava.awt.headless=true"

#execute
mvn package dependency:copy-dependencies -DskipTests
java $JAVA_OPTS -cp $CLASS_PATH $MAIN_CLASS >console.log 2>&1 &
sleep 3
tail -f console.log