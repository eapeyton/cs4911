"use strict";

module.exports = {
  up: function(migration, DataTypes, done) {
    migration
      .createTable('Rooms', {
        id: { 
          type:  DataTypes.UUIDV4, 
          primaryKey: true  
        },
        name: DataTypes.STRING,
        maxPlayers: DataTypes.INTEGER,
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
