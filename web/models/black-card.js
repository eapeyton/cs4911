module.exports = function(sequelize, DataTypes) {
  var BlackCard = sequelize.define("BlackCard", {
    id: { type: DataTypes.UUID, defaultValue: DataTypes.UUIDV4 },
    text: DataTypes.STRING,
    createdAt: DataTypes.DATE,
    updatedAt: DataTypes.DATE
  }, {
    classMethods: {
      associate: function(models) {
        BlackCard.belongsTo(models.User, { foreignKey: 'creator' });
      }
    }
  });

  return BlackCard;
};