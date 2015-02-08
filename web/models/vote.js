module.exports = function(sequelize, DataTypes) {
  var Vote = sequelize.define("Vote", {
    id: { 
      type: DataTypes.UUIDV4,
      defaultValue: DataTypes.UUIDV4,
      unique: true,
      primaryKey: true
    },
    uid: {
      type: DataTypes.UUIDV4,
      references: 'Users',
      referencesKey: 'id',
      allowNull: false
    },
    cid: {
      type: DataTypes.UUIDV4,
      references: 'Cards',
      referencesKey: 'id',
      allowNull: false
    },
    upvoted: DataTypes.BOOLEAN,
    createdAt: DataTypes.DATE,
    updatedAt: DataTypes.DATE
  }, {
    classMethods: {
      associate: function(models) {
        Vote.belongsTo(models.User, { foreignKey: 'uid' });
        Vote.belongsTo(models.Card, { foreignKey: 'cid',
      references: 'Users',
      referencesKey: 'id' });
      }
    }
  });
  return Vote;
};