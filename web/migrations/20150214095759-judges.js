"use strict";

module.exports = {
  up: function(migration, DataTypes, done) {
    migration
      .createTable('Judges', {
        id: { 
          type:  DataTypes.UUID, 
          defaultValue: DataTypes.UUIDV4,
          unique: true,
          primaryKey: true
        },
        userId: {
          type: DataTypes.UUID,
          references: 'Users',
          referencesKey: 'id',
          allowNull: false,
          unique: 'userRoomPlaceIndex'
        },
        roomId: {
          type: DataTypes.UUID,
          references: 'Rooms',
          referencesKey: 'id',
          allowNull: false,
          unique: 'userRoomPlaceIndex'
        },
        place: {
          type: DataTypes.INTEGER,
          allowNull: false,
          unique: 'userRoomPlaceIndex'
        },
        createdAt: DataTypes.DATE,
        updatedAt: DataTypes.DATE
      })
      .complete(done);
  },

  down: function(migration, DataTypes, done) {
    migration
      .dropTable('Judges')
      .complete(done);
  }
};