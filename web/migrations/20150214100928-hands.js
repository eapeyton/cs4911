"use strict";

module.exports = {
  up: function(migration, DataTypes, done) {
    migration
      .createTable('Hands', {
        id: { 
          type:  DataTypes.UUID, 
          defaultValue: DataTypes.UUIDV4,
          unique: true,
          primaryKey: true
        },
        cardId: {
          type: DataTypes.UUID,
          references: 'Cards',
          referencesKey: 'id',
          allowNull: false
        },
        gameId: {
          type: DataTypes.UUID,
          references: 'Games',
          referencesKey: 'id',
          allowNull: false
        },
        userId: {
          type: DataTypes.UUID,
          references: 'Users',
          referencesKey: 'id',
          allowNull: false
        },
        played: DataTypes.BOOLEAN,
        createdAt: DataTypes.DATE,
        updatedAt: DataTypes.DATE
      })
      .complete(done);
  },

  down: function(migration, DataTypes, done) {
    migration
      .dropTable('Hands')
      .complete(done);
  }
};