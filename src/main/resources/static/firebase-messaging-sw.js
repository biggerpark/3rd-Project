importScripts(
  "https://www.gstatic.com/firebasejs/10.7.2/firebase-app-compat.js",
);
importScripts(
  "https://www.gstatic.com/firebasejs/10.7.2/firebase-messaging-compat.js",
);

firebase.initializeApp({
  apiKey: "AIzaSyBfBBMRunQDKWqL9pXu6_oTJ6KZYpjfUcQ",
  authDomain: "job-done-4b007.firebaseapp.com",
  projectId: "job-done-4b007",
  storageBucket: "job-done-4b007.firebasestorage.app",
  messagingSenderId: "783585960523",
  appId: "1:783585960523:web:d3c3dce0acfc1f820223ce",
});

const messaging = firebase.messaging();

messaging.onBackgroundMessage(payload => {
  console.log("백그라운드 푸시 알림: ", payload);
  self.registration.showNotification(payload.notification.title, {
    body: payload.notification.body,
    icon: "/firebase-logo.png",
  });
});
