const functions = require('firebase-functions')
const admin = require('firebase-admin')

admin.initializeApp(functions.config().firebase)

exports.notifyGlobalNewOdorEvent = functions.database.ref('/events/{id}').onWrite(event => {
  var odorEventName = event.data.child('name').val()
  var odorEventOwner = event.data.child('owner-id').val()

  var payload = {
    notification: {
      title: 'New Odor Event: ' + odorEventName,
      body: odorEventOwner + ' reported ' + odorEventName
    }
  }

  admin.messaging().sendToTopic('newOdorEvents', payload)

  return null
})
