module.exports = function(sequelize, DataTypes) {
  var Vote = sequelize.define("Vote", {
    id: { 
      type: DataTypes.UUID,
      defaultValue: DataTypes.UUIDV4,
      unique: true,
      primaryKey: true
    },
    userId: {
      type: DataTypes.UUID,
      references: 'Users',
      referencesKey: 'id',
      allowNull: false
    },
    cardId: {
      type: DataTypes.UUID,
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
        Vote.belongsTo(models.User, { foreignKey: 'userId' });
        Vote.belongsTo(models.Card, { foreignKey: 'cardId' });
      }
    }
  });
  return Vote;
};