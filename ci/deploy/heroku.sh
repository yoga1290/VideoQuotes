#############################################
# Generate a new random keys for OAuth2/JWT #
#############################################

# points to resource directory:
KEYSTORE='src/main/resources/keystore.jks';
PUBLIC_KEY='src/main/resources/public_key.txt';

# Random Hex w no spacing
PASSWORD=$(echo $(od -vAn -N80 -tx < /dev/urandom) | sed 's/ //g');
APP_NAME='videoquotes';


# Remove old Java Keystore (if exists):
rm $KEYSTORE;

# Generate public/private keys:
keytool -genkeypair -alias $APP_NAME -keyalg RSA -keypass $PASSWORD -keystore $KEYSTORE -storepass $PASSWORD -dname 'CN=Youssef Gamil, OU=orgUnit, O=org, L=city, S=state, C=EG';

# Extract the Public key to $PUBLIC_KEY file:
keytool -list -rfc --keystore $KEYSTORE -storepass $PASSWORD | openssl x509 -inform pem -pubkey | head -9 >$PUBLIC_KEY;

# Update oauth2server.properties:
echo 'oauth2.storepass='$PASSWORD'\noauth2.key='$APP_NAME >src/main/resources/oauth2server.properties;

# Copy configuration
cp ci/prod/*.properties src/main/resources;

#####################################################
# Clean, Build and push to Heroku as Git Repository #
#####################################################
#
mvn clean package;

rm -fr heroku;
mkdir heroku;
cp -r src heroku;
cp pom.xml heroku;

JAR_FILE=$(ls target/*.jar | head -1);
echo "web java -Dserver.port=\$PORT \$JAVA_OPTS -jar $JAR_FILE" >heroku/Procfile

cd heroku \
    && git init \
    && git add . \
    && git commit -m "deploy" \
    && heroku git:remote -a $APP_NAME \
    && git push heroku master --force;
