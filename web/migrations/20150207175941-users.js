"use strict";

module.exports = {
  up: function(migration, DataTypes, done) {
    migration
      .createTable('Users', {
        id: { 
          type:  DataTypes.UUIDV4, 
          defaultValue: DataTypes.UUIDV4,
          unique: true,
          primaryKey: true
        },
        rid: {
          type: DataTypes.UUIDV4,
          references: 'Rooms',
          referencesKey: 'id',
          allowNull: true
        }
        fbid: {
          type: DataTypes.STRING,
          unique: true,
          allowNull: false
        },
        fbToken: {
          type: DataTypes.STRING,
          allowNull: false
        },
        name: DataTypes.STRING,
        pic: DataTypes.STRING,
        createdAt: DataTypes.DATE,
        updatedAt: DataTypes.DATE
      })
      .complete(done);
  },

  down: function(migration, DataTypes, done) {
    migration
      .dropTable('Users')
      .complete(done);
  }
};