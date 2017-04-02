const functions = require('firebase-functions')
const admin = require('firebase-admin')
admin.initializeApp(functions.config().firebase)

exports.notifyGlobalNewOdorEvent = functions.database.ref('/events/{id}').onWrite(event => {
  if (!event.data.exists() || event.data.previous.exists()) {
    // TODO: Check if this is inefficient because it's triggered on
    // every write to every event. There might be a way to listen only
    // to new events by changing the path of the database ref above.
    return null
  }

  var odorEvent = event.data.val()
  var reportId = odorEvent.reportids[0]

  return admin.database().ref('/reports').child(reportId).once('value').then(snapshot => {
    var payload = {
      data: {
        event: JSON.stringify(odorEvent),
        report: JSON.stringify(snapshot.val())
      }
    }

    admin.messaging().sendToTopic('newOdorEvents', payload)
  })
})
