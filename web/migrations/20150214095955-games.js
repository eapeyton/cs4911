"use strict";

module.exports = {
  up: function(migration, DataTypes, done) {
    migration
      .createTable('Games', {
        id: { 
          type:  DataTypes.UUID, 
          defaultValue: DataTypes.UUIDV4,
          unique: true,
          primaryKey: true
        },
        roomId: {
          type: DataTypes.UUID,
          references: 'Rooms',
          referencesKey: 'id',
          allowNull: false
        },
        finishTime: DataTypes.DATE,
        createdAt: DataTypes.DATE,
        updatedAt: DataTypes.DATE
      })
      .complete(done);
  },

  down: function(migration, DataTypes, done) {
    migration
      .dropTable('Games')
      .complete(done);
  }
};