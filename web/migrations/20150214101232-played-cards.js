"use strict";

module.exports = {
  up: function(migration, DataTypes, done) {
    migration
      .createTable('PlayedCard', {
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
          unique: 'userCardRoundIndex'
        },
        cardId: {
          type: DataTypes.UUID,
          references: 'Cards',
          referencesKey: 'id',
          allowNull: false,
          unique: 'userCardRoundIndex'
        },
        roundId: {
          type: DataTypes.UUID,
          references: 'Rounds',
          referencesKey: 'id',
          allowNull: false,
          unique: 'userCardRoundIndex'
        },
        createdAt: DataTypes.DATE,
        updatedAt: DataTypes.DATE
      })
      .complete(done);
  },

  down: function(migration, DataTypes, done) {
    migration
      .dropTable('PlayedCard')
      .complete(done);
  }
};