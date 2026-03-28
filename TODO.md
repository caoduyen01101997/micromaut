# Pump Telegram Fix TODO

## 1. Fix Gradle + Start ✅\n```
rm -rf ~/.gradle/caches/
./gradlew wrapper
./gradlew run
```
```
cd /Users/duyenpham/Desktop/source-code/micromaut
./gradlew run
```

## 2. Verify startup logs
- [ ] RemindService created WITH PumpService
- [ ] Firebase initialized ✅
- [ ] Bot connection successful
- [ ] Polling Telegram every 5s

## 3. Test correct command
Send `/pump_on` (underscore):
- [ ] Logs show "💧 Executing /pump_on command"
- [ ] Telegram confirms "✅ Máy bơm đã được BẬT"
- [ ] HTTP GET http://localhost:8080/pump/status → {"success":true,"state":"ON"}

## 4. Optional: Support space commands
Edit RemindService.handleCommand():
```java
case "/pump on":
case "/pump_on":
    // handle pump on
```

## 5. Fix Firebase Write Error ✅
Firebase Console → Realtime DB → Rules:
```
{
  "rules": {
    "pump": { ".read": true, ".write": true }
  }
}
```
Test: `curl -X PUT -d '"ON"' https://autowaterplant-3d78d-default-rtdb.asia-southeast1.firebasedatabase.app/pump/state.json`

## Status: Pump fully working after rules fix!
