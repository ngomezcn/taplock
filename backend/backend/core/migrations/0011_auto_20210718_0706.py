# Generated by Django 3.2.4 on 2021-07-18 07:06

from django.db import migrations, models
import uuid


class Migration(migrations.Migration):

    dependencies = [
        ('core', '0010_auto_20210718_0702'),
    ]

    operations = [
        migrations.AlterField(
            model_name='itap',
            name='activation_key',
            field=models.CharField(blank=True, max_length=8, null=True, unique=True),
        ),
        migrations.AlterField(
            model_name='itap',
            name='seed',
            field=models.CharField(default=uuid.UUID('1d42b1ad-468f-42a3-a7d8-baf1663b0fc8'), max_length=36),
        ),
    ]
