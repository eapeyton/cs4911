module.exports = function(sequelize, DataTypes) {
  var WhiteCard = sequelize.define("WhiteCard", {
    id: { type: DataTypes.UUID, defaultValue: DataTypes.UUIDV4 },
    text: DataTypes.STRING,
    createdAt: DataTypes.DATE,
    updatedAt: DataTypes.DATE
  }, {
    classMethods: {
      associate: function(models) {
        WhiteCard.belongsTo(models.User, { foreignKey: 'creator' });
      }
    }
  });

  return WhiteCard;
};