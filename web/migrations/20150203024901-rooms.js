"use strict";

module.exports = {
  up: function(migration, DataTypes, done) {
    migration
      .createTable('Rooms', {
        id: { 
          type: DataTypes.INTEGER,
          primaryKey: true
        },
        name: DataTypes.STRING,
        createdAt: DataTypes.DATE,
        updatedAt: DataTypes.DATE
      })
      .complete(done)
  },

  down: function(migration, DataTypes, done) {
    migration
      .dropTable('Rooms')
      .complete(done)
  }
};
