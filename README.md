# VK Java bot
### What is it?

It's my small vk bot that forwards all incoming emails from my mail account to myself in *vkontakte* social network.

I use *VK SDK* to send messages and *javax.mail* to read incoming emails.
### Any prerequisites to run this thing?

Currently, I didn't find out how to properly run this app in docker container (it refuses to connect to my email provider there),
so instead you need to run it in raw state. I mean you need `JDK11` and `maven`.

### OK, How to run this thing?

I personally run it something like that
```shell script
nohup mvn spring-boot:run &
``` 
