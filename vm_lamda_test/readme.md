1. Install AWS Command Line Interface `brew install awscli`
2. Create Jar Archive `mvn package`
3. Deploy changes to AWS `aws lambda update-function-code --function-name vmTest --zip-file fileb://{path-to-jar-fife}`
4. At input test event put a String, not a json. To test a json use `com.inenergis.egamas.Test` as entry point
```
$ echo '"Victor"' | http POST https://2eu2n7suyc.execute-api.us-east-1.amazonaws.com/vm_test/vm-test
HTTP/1.1 200 OK
Connection: keep-alive
Content-Length: 25
Content-Type: application/json
Date: Wed, 07 Jun 2017 15:31:42 GMT
Via: 1.1 6bb7bc4d29fd92cbd09ff6b6a1806568.cloudfront.net (CloudFront)
X-Amz-Cf-Id: UZ0CRG2731WObXRAplZRut6ceqOI5HrMWsI8UaGVRgMGbNTIg64D-g==
X-Amzn-Trace-Id: sampled=0;root=1-59381c5e-bfeabaa29493615acf50afbc
X-Cache: Miss from cloudfront
x-amzn-RequestId: 68847cd4-4b96-11e7-9341-1b7f9ee950c4

"Hello, Victor! IntelliJ"
```